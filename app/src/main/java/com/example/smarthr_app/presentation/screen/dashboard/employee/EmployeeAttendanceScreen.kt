package com.example.smarthr_app.presentation.screen.dashboard.employee

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smarthr_app.data.model.AttendanceResponseDto
import com.example.smarthr_app.presentation.theme.PrimaryPurple
import com.example.smarthr_app.presentation.theme.CrewHQCardBg
import com.example.smarthr_app.presentation.theme.TextSecondaryDark
import com.example.smarthr_app.presentation.viewmodel.AttendanceViewModel
import com.example.smarthr_app.utils.LocationHelper
import com.example.smarthr_app.utils.Resource
import com.example.smarthr_app.utils.ToastHelper
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.smarthr_app.presentation.components.CompanyLockScreen
import com.example.smarthr_app.presentation.viewmodel.AuthViewModel
import java.time.YearMonth
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip


@Composable
fun EmployeeAttendanceScreen(
    attendanceViewModel: AttendanceViewModel,
    authViewModel: AuthViewModel,
    onNavigateToCompanyManagement: () -> Unit
) {
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }
    val coroutineScope = rememberCoroutineScope()
    var currentYearMonth by remember { mutableStateOf(java.time.YearMonth.now()) }

    val officeLocationState by attendanceViewModel.officeLocationState.collectAsState(initial = null)
    val markAttendanceState by attendanceViewModel.markAttendanceState.collectAsState(initial = null)
    val attendanceHistoryState by attendanceViewModel.attendanceHistoryState.collectAsState(initial = null)
    val user by authViewModel.user.collectAsState(initial = null)

    // Check if user has joined a company
    val hasJoinedCompany = !user?.companyCode.isNullOrBlank()
    val isWaitlisted = !user?.waitingCompanyCode.isNullOrBlank()

    var isLocationPermissionGranted by remember { mutableStateOf(false) }

    var odPurpose by remember { mutableStateOf("") }
    var odAssignedBy by remember { mutableStateOf("") }
    var odLocation by remember { mutableStateOf("") }
    val onDutyHistory by authViewModel.onDutyHistory.collectAsState(initial = emptyList())

    // Show lock screen if user hasn't joined a company
    if (!hasJoinedCompany && !isWaitlisted) {
        CompanyLockScreen(
            title = "Attendance Feature Locked",
            onJoinCompanyClick = onNavigateToCompanyManagement
        )
        return
    }

    // Show waiting message if user is waitlisted
    if (isWaitlisted && !hasJoinedCompany) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Waiting for approval",
                    modifier = Modifier.size(80.dp),
                    tint = Color(0xFFFF9800)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Waiting for HR Approval",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your request to join ${user?.waitingCompanyCode} is pending. HR will review your request soon.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        return
    }

    // Get current attendance status from history
    val currentAttendanceStatus = remember(attendanceHistoryState) {
        when (val state = attendanceHistoryState) {
            is Resource.Success -> {
                // Get today's attendance record
                val today = java.time.LocalDate.now().toString()
                val todayRecord = state.data.find { attendance ->
                    attendance.checkIn?.startsWith(today) == true
                }
                when {
                    todayRecord == null -> "NONE" // No check-in today
                    todayRecord.checkOut != null -> "CHECKOUT" // Already checked out
                    else -> "CHECKIN" // Checked in but not out
                }
            }
            else -> "NONE"
        }
    }

    // Location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isLocationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        // Request location permissions
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        // Load initial data only if user has joined company
        if (hasJoinedCompany) {
            attendanceViewModel.loadOfficeLocation()
            attendanceViewModel.loadAttendanceHistory()
        }

    }

    // Handle mark attendance response
    LaunchedEffect(markAttendanceState) {
        when (val state = markAttendanceState) {
            is Resource.Success -> {
                val actionText = if (state.data.checkOut != null) "Checked out successfully!"
                else "Checked in successfully!"
                ToastHelper.showSuccessToast(context, actionText)
                attendanceViewModel.clearMarkAttendanceState()
                attendanceViewModel.loadAttendanceHistory()
            }
            is Resource.Error -> {
                ToastHelper.showErrorToast(context, state.message)
                attendanceViewModel.clearMarkAttendanceState()
            }
            else -> {}
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Calendar at the absolute top
        when (val historyState = attendanceHistoryState) {
            is Resource.Success -> {
                item {
                    AttendanceCalendar(
                        currentYearMonth = currentYearMonth,
                        onMonthChange = { currentYearMonth = it },
                        attendanceRecords = historyState.data
                    )
                }
            }
            is Resource.Loading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryPurple)
                    }
                }
            }
            else -> {}
        }

        // Office Location Card
        item {
            when (val officeState = officeLocationState) {
                is Resource.Success -> {
                    val office = officeState.data
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CrewHQCardBg),
                        border = BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Office Location",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "You must be within ${office.radius}m of the office to check in",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryDark
                                )
                            }
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.08f)))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Location:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondaryDark
                                )
                                Text(
                                    text = "${user?.companyCode ?: "Company"} Office",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Radius:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondaryDark
                                )
                                Text(
                                    text = "${office.radius} meters",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                else -> {}
            }
        }

        // 2. Today's Check-In / Check-Out Controls Box
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(PrimaryPurple, PrimaryPurple.copy(alpha = 0.8f))
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header text
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Let's Clock-In!",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Don't miss your clock-in schedule",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        // Clock icon
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Clock",
                            modifier = Modifier.size(48.dp),
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Today's Status Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Today's Status",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            when (val historyState = attendanceHistoryState) {
                                is Resource.Success -> {
                                    val todayRecord = historyState.data.find { attendance ->
                                        attendance.checkIn?.startsWith(java.time.LocalDate.now().toString()) == true
                                    }

                                    if (todayRecord != null) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text(
                                                    text = "Check In",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.Black
                                                )
                                                Text(
                                                    text = formatTime(todayRecord.checkIn ?: ""),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                            }

                                            Column(horizontalAlignment = Alignment.End) {
                                                Text(
                                                    text = "Check Out",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.Black
                                                )
                                                Text(
                                                    text = if (todayRecord.checkOut != null) formatTime(todayRecord.checkOut) else "Not yet",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                            }
                                        }
                                    } else {
                                        Text(
                                            text = "No attendance recorded for today",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Black
                                        )
                                    }
                                }
                                is Resource.Loading -> {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = PrimaryPurple
                                        )
                                    }
                                }
                                else -> {
                                    Text(
                                        text = "Unable to load today's status",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Check-in/Check-out button
                    Button(
                        onClick = {
                            if (!isLocationPermissionGranted) {
                                ToastHelper.showErrorToast(context, "Location permission required")
                                return@Button
                            }

                            when (val officeState = officeLocationState) {
                                is Resource.Success -> {
                                    coroutineScope.launch {
                                        markAttendance(
                                            attendanceViewModel = attendanceViewModel,
                                            locationHelper = locationHelper,
                                            officeLocation = officeState.data,
                                            currentStatus = currentAttendanceStatus,
                                            context = context
                                        )
                                    }
                                }
                                is Resource.Error -> {
                                    ToastHelper.showErrorToast(context, "Office location not set by HR")
                                }
                                else -> {
                                    ToastHelper.showErrorToast(context, "Loading office location...")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (currentAttendanceStatus) {
                                "CHECKIN" -> Color(0xFFFF5722) // Red for checkout
                                else -> Color(0xFF4CAF50) // Green for checkin
                            }
                        ),
                        enabled = markAttendanceState !is Resource.Loading && currentAttendanceStatus != "CHECKOUT"
                    ) {
                        if (markAttendanceState is Resource.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = when (currentAttendanceStatus) {
                                    "CHECKIN" -> "Check Out"
                                    "CHECKOUT" -> "Already Checked Out"
                                    else -> "Check In"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 2b. On Duty (OD) Card Form
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CrewHQCardBg),
                border = BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE53935).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Work,
                                contentDescription = null,
                                tint = Color(0xFFE53935),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "On Duty (OD)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Going out for office work? Fill details below",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    // Field 1: Purpose of Going
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFE53935).copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Work,
                                    contentDescription = null,
                                    tint = Color(0xFFE53935),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Purpose of Going",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        OutlinedTextField(
                            value = odPurpose,
                            onValueChange = { odPurpose = it },
                            placeholder = { Text("e.g., Client meeting, Site visit...", color = Color.White.copy(alpha = 0.4f)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryPurple,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }

                    // Field 2: Task Assigned By
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PrimaryPurple.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = PrimaryPurple,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Task Assigned By",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        OutlinedTextField(
                            value = odAssignedBy,
                            onValueChange = { odAssignedBy = it },
                            placeholder = { Text("e.g., Manager name...", color = Color.White.copy(alpha = 0.4f)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryPurple,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }

                    // Field 3: Location Name
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF4CAF50).copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Location Name",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        OutlinedTextField(
                            value = odLocation,
                            onValueChange = { odLocation = it },
                            placeholder = { Text("e.g., Bhiwadi site, Delhi office...", color = Color.White.copy(alpha = 0.4f)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryPurple,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Green Button Check-in for OD
                    Button(
                        onClick = {
                            if (odPurpose.isBlank() || odAssignedBy.isBlank() || odLocation.isBlank()) {
                                ToastHelper.showErrorToast(context, "Please fill all OD details")
                                return@Button
                            }
                            val checkInTime = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"))
                            val record = com.example.smarthr_app.data.model.OnDutyRecord(
                                purpose = odPurpose,
                                assignedBy = odAssignedBy,
                                locationName = odLocation,
                                checkInTime = checkInTime
                            )
                            authViewModel.saveOnDutyRecord(record)
                            ToastHelper.showSuccessToast(context, "Checked in for On Duty successfully!")
                            odPurpose = ""
                            odAssignedBy = ""
                            odLocation = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Check in for OD",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Text(
                        text = "Your photo and GPS location will be captured for verification.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                    )
                }
            }
        }

        // 2c. OD History Card List
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CrewHQCardBg),
                border = BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PrimaryPurple.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = PrimaryPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "OD History",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Your past on-duty records will appear here",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    if (onDutyHistory.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.05f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Work,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.3f),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = "No OD records yet",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Use the form above to start your first on-duty session.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            onDutyHistory.forEachIndexed { index, record ->
                                if (index > 0) {
                                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.08f)))
                                }
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = record.purpose,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = record.checkInTime,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4CAF50)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Location: ${record.locationName}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextSecondaryDark
                                        )
                                        Text(
                                            text = "By: ${record.assignedBy}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextSecondaryDark
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Attendance History Header & Records
        when (val historyState = attendanceHistoryState) {
            is Resource.Success -> {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Attendance History",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                items(historyState.data) { record ->
                    AttendanceRecordCard(record = record)
                }
            }
            is Resource.Loading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryPurple)
                    }
                }
            }
            is Resource.Error -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Error loading attendance history",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun AttendanceRecordCard(record: AttendanceResponseDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Date
            Text(
                text = formatDisplayDate(record.checkIn ?: ""),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Check In Time
                Column {
                    Text(
                        text = "Check In",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (record.checkIn != null) formatTime(record.checkIn) else "Not recorded",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Check Out Time
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Check Out",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (record.checkOut != null) formatTime(record.checkOut) else "Not recorded",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

private suspend fun markAttendance(
    attendanceViewModel: AttendanceViewModel,
    locationHelper: LocationHelper,
    officeLocation: com.example.smarthr_app.data.model.OfficeLocationResponseDto,
    currentStatus: String,
    context: android.content.Context
) {
    val currentLocation = locationHelper.getCurrentLocation()
    if (currentLocation == null) {
        ToastHelper.showErrorToast(context, "Unable to get current location")
        return
    }

    val currentLat = currentLocation.latitude.toString()
    val currentLng = currentLocation.longitude.toString()

    val isWithinRange = locationHelper.isWithinRadius(
        currentLat,
        currentLng,
        officeLocation.latitude,
        officeLocation.longitude,
        officeLocation.radius
    )

    if (!isWithinRange) {
        val distance = locationHelper.getDistanceInMeters(
            currentLat,
            currentLng,
            officeLocation.latitude,
            officeLocation.longitude
        )
        ToastHelper.showErrorToast(
            context,
            "You are ${distance.toInt()}m away from office. Please come closer to mark attendance."
        )
        return
    }

    val attendanceType = if (currentStatus == "CHECKIN") "CHECKOUT" else "CHECKIN"
    attendanceViewModel.markAttendance(
        attendanceType,
        currentLat,
        currentLng
    )
}

private fun formatDisplayDate(dateString: String): String {
    return try {
        val dateTime = java.time.LocalDateTime.parse(dateString.replace("Z", ""))
        val date = dateTime.toLocalDate()
        "${date.dayOfMonth} ${date.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${date.year}"
    } catch (e: Exception) {
        dateString
    }
}

private fun formatTime(timeString: String): String {
    return try {
        val dateTime = java.time.LocalDateTime.parse(timeString.replace("Z", ""))
        val time = dateTime.toLocalTime()
        time.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        timeString
    }
}

@Composable
fun AttendanceCalendar(
    currentYearMonth: java.time.YearMonth,
    onMonthChange: (java.time.YearMonth) -> Unit,
    attendanceRecords: List<AttendanceResponseDto>
) {
    // Days in week headers
    val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")

    // Calculations
    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfMonth = currentYearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value // 1 = Monday, 7 = Sunday
    val emptySlotsBefore = firstDayOfWeek - 1

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Calendar Header: Month Name + Navigation Arrows
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onMonthChange(currentYearMonth.minusMonths(1)) }
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Previous Month",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "${currentYearMonth.month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault())} ${currentYearMonth.year}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(
                    onClick = { onMonthChange(currentYearMonth.plusMonths(1)) }
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next Month",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Week Day Headers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Monthly Days Grid
            val totalSlots = emptySlotsBefore + daysInMonth
            val rowsCount = (totalSlots + 6) / 7

            for (rowIndex in 0 until rowsCount) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (colIndex in 0 until 7) {
                        val slotIndex = rowIndex * 7 + colIndex
                        val dayNumber = slotIndex - emptySlotsBefore + 1

                        if (slotIndex < emptySlotsBefore || dayNumber > daysInMonth) {
                            // Empty placeholder box
                            Box(modifier = Modifier.size(36.dp))
                        } else {
                            val date = currentYearMonth.atDay(dayNumber)
                            val isToday = date == java.time.LocalDate.now()

                            // Get status for this date
                            val record = attendanceRecords.find { rec ->
                                rec.checkIn?.startsWith(date.toString()) == true
                            }

                            val (statusColor, showIndicator) = when {
                                record != null -> {
                                    if (record.checkOut != null) {
                                        Color(0xFF4CAF50) to true // Checked in and out (Green)
                                    } else {
                                        Color(0xFFFF9800) to true // Checked in but not out (Orange)
                                    }
                                }
                                date.isBefore(java.time.LocalDate.now()) -> {
                                    // Weekdays in the past with no record are marked as absent (Red)
                                    val dayOfWeekVal = date.dayOfWeek.value
                                    if (dayOfWeekVal in 1..5) {
                                        Color(0xFFF44336) to true // Absent (Red)
                                    } else {
                                        Color.Transparent to false // Weekend
                                    }
                                }
                                else -> Color.Transparent to false
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isToday) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                        else Color.Transparent
                                    )
                                    .border(
                                        width = if (isToday) 1.dp else 0.dp,
                                        color = if (isToday) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = CircleShape
                                    )
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )

                                if (showIndicator) {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 2.dp)
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(statusColor)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Legend explanation
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(label = "Present", color = Color(0xFF4CAF50))
                LegendItem(label = "Missing Out", color = Color(0xFFFF9800))
                LegendItem(label = "Absent", color = Color(0xFFF44336))
            }
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}