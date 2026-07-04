package com.example.smarthr_app.presentation.screen.dashboard.employee

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.smarthr_app.presentation.screen.dashboard.hr.ProfileInfoRow
import com.example.smarthr_app.presentation.theme.PrimaryPurple
import com.example.smarthr_app.presentation.theme.CrewHQCardBg
import com.example.smarthr_app.presentation.theme.TextSecondaryDark
import com.example.smarthr_app.presentation.viewmodel.AuthViewModel
import com.example.smarthr_app.utils.Resource
import com.example.smarthr_app.utils.ToastHelper
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeProfileScreen(
    authViewModel: AuthViewModel,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToCompanyManagement: () -> Unit,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val user by authViewModel.user.collectAsState(initial = null)
    val extraDetails by authViewModel.extraProfileDetails.collectAsState(initial = null)
    var showLogoutDialog by remember { mutableStateOf(false) }
    var isGeofenceActive by remember { mutableStateOf(true) }
    var isRemindersEnabled by remember { mutableStateOf(false) }

    // Refresh profile data on screen load
    LaunchedEffect(Unit) {
        authViewModel.refreshProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent) // Transparent to show glowing dark gradient background
            .statusBarsPadding()
    ) {
        // Custom Header mimicking screenshot (Transparent background, white title)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "My Profile",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "View and manage your personal information",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark
                        )
                    }
                }

                IconButton(onClick = { showLogoutDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            }
        }

        // Scrollable Profile Sections
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Profile Avatar & Edit Profile Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CrewHQCardBg),
                border = BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Circular Avatar with Initials
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(PrimaryPurple.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getInitials(user?.name ?: "Anmol Saini"),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryPurple
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = user?.name ?: "Anmol Saini",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = user?.email ?: "anmolsaini01881@gmail.com",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    // Purple Edit Profile Button matching theme
                    Button(
                        onClick = onNavigateToEditProfile,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Edit Profile",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 2. Basic Information Card
            MockProfileSectionCard(
                title = "Basic Information",
                icon = Icons.Default.Person
            ) {
                MockInfoRow(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = user?.email ?: "anmolsaini01881@gmail.com"
                )
                Spacer(modifier = Modifier.height(12.dp))
                MockInfoRow(
                    icon = Icons.Default.Phone,
                    label = "Phone",
                    value = if (!user?.phone.isNullOrBlank()) user!!.phone!! else "Not provided"
                )
                Spacer(modifier = Modifier.height(12.dp))
                MockInfoRow(
                    icon = Icons.Default.Schedule,
                    label = "Date of Joining",
                    value = "2 July 2026"
                )
                Spacer(modifier = Modifier.height(12.dp))
                MockInfoRow(
                    icon = Icons.Default.Info,
                    label = "Aadhar Number",
                    value = if (!extraDetails?.aadhar.isNullOrBlank()) extraDetails!!.aadhar!! else "Not provided"
                )
            }

            // 3. Personal Information Card
            MockProfileSectionCard(
                title = "Personal Information",
                icon = Icons.Default.Person
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    MockInfoField(
                        label = "Gender",
                        value = when (user?.gender) {
                            "M" -> "Male"
                            "F" -> "Female"
                            else -> "Not Provided"
                        },
                        modifier = Modifier.weight(1f)
                    )
                    MockInfoField(
                        label = "Marital Status",
                        value = if (!extraDetails?.maritalStatus.isNullOrBlank()) extraDetails!!.maritalStatus!! else "Not Provided",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    MockInfoField(
                        label = "Blood Group",
                        value = if (!extraDetails?.bloodGroup.isNullOrBlank()) extraDetails!!.bloodGroup!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                    MockInfoField(
                        label = "Physically Challenged",
                        value = if (!extraDetails?.physicallyChallenged.isNullOrBlank()) extraDetails!!.physicallyChallenged!! else "No",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                MockInfoField(
                    label = "Current Address",
                    value = if (!extraDetails?.currentAddress.isNullOrBlank()) extraDetails!!.currentAddress!! else "Not provided",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                MockInfoField(
                    label = "Permanent Address",
                    value = if (!extraDetails?.permanentAddress.isNullOrBlank()) extraDetails!!.permanentAddress!! else "Not provided",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 4. Family Information Card
            MockProfileSectionCard(
                title = "Family Information",
                icon = Icons.Default.Group
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    MockInfoField(
                        label = "Father's Name",
                        value = if (!extraDetails?.fathersName.isNullOrBlank()) extraDetails!!.fathersName!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                    MockInfoField(
                        label = "Mother's Name",
                        value = if (!extraDetails?.mothersName.isNullOrBlank()) extraDetails!!.mothersName!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 5. Emergency Contact Card
            MockProfileSectionCard(
                title = "Emergency Contact",
                icon = Icons.Default.Phone
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    MockInfoField(
                        label = "Contact Name",
                        value = if (!extraDetails?.emergencyName.isNullOrBlank()) extraDetails!!.emergencyName!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                    MockInfoField(
                        label = "Contact Number",
                        value = if (!extraDetails?.emergencyNumber.isNullOrBlank()) extraDetails!!.emergencyNumber!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                    MockInfoField(
                        label = "Relation",
                        value = if (!extraDetails?.emergencyRelation.isNullOrBlank()) extraDetails!!.emergencyRelation!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 6. Bank Details Card
            MockProfileSectionCard(
                title = "Bank Details",
                icon = Icons.Default.Info
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    MockInfoField(
                        label = "Bank Name",
                        value = if (!extraDetails?.bankName.isNullOrBlank()) extraDetails!!.bankName!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                    MockInfoField(
                        label = "Account Holder Name",
                        value = if (!extraDetails?.accountHolder.isNullOrBlank()) extraDetails!!.accountHolder!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    MockInfoField(
                        label = "Account Number",
                        value = if (!extraDetails?.accountNumber.isNullOrBlank()) extraDetails!!.accountNumber!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                    MockInfoField(
                        label = "IFSC Code",
                        value = if (!extraDetails?.ifscCode.isNullOrBlank()) extraDetails!!.ifscCode!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                MockInfoField(
                    label = "UPI ID",
                    value = if (!extraDetails?.upiId.isNullOrBlank()) extraDetails!!.upiId!! else "Not provided",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 7. Employment Information Card
            MockProfileSectionCard(
                title = "Employment Information",
                icon = Icons.Default.Work
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    MockInfoField(
                        label = "UAN",
                        value = if (!extraDetails?.uan.isNullOrBlank()) extraDetails!!.uan!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                    MockInfoField(
                        label = "PAN Number",
                        value = if (!extraDetails?.pan.isNullOrBlank()) extraDetails!!.pan!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    MockInfoField(
                        label = "PF Number",
                        value = if (!extraDetails?.pfNumber.isNullOrBlank()) extraDetails!!.pfNumber!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                    MockInfoField(
                        label = "PF Joining Date",
                        value = if (!extraDetails?.pfJoining.isNullOrBlank()) extraDetails!!.pfJoining!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    MockInfoField(
                        label = "ESI Number",
                        value = if (!extraDetails?.esiNumber.isNullOrBlank()) extraDetails!!.esiNumber!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                    MockInfoField(
                        label = "ESI Joining Date",
                        value = if (!extraDetails?.esiJoining.isNullOrBlank()) extraDetails!!.esiJoining!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    MockInfoField(
                        label = "EPS Number",
                        value = if (!extraDetails?.epsNumber.isNullOrBlank()) extraDetails!!.epsNumber!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                    MockInfoField(
                        label = "EPS Exit Date",
                        value = if (!extraDetails?.epsExit.isNullOrBlank()) extraDetails!!.epsExit!! else "Not provided",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 8. Check-Out Reminders Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CrewHQCardBg),
                border = BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Check-Out Reminders",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Get notified at 6 PM if you forget to check out",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Warning Alert Card mimicking the screenshot (themed to purple)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = PrimaryPurple.copy(alpha = 0.1f)),
                        border = BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = PrimaryPurple,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Enable Check-Out Reminders\nGet notified at 6 PM if you forget to check out - works even when app is closed.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Enable Reminders Button
                    Button(
                        onClick = { isRemindersEnabled = !isRemindersEnabled },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRemindersEnabled) Color(0xFF4CAF50) else PrimaryPurple
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isRemindersEnabled) "Reminders Enabled" else "Enable Reminders",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "You'll be asked to allow notifications.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 9. Geofence Control Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CrewHQCardBg),
                border = BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info, // Placeholder for Geofence/Globe icon
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Geofence Control",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Enable or disable location-based check-in/check-out verification",
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Geofence Active",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isGeofenceActive) "ON" else "OFF",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isGeofenceActive) Color(0xFF10B981) else Color.Red
                            )
                        }

                        Button(
                            onClick = { isGeofenceActive = !isGeofenceActive },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = if (isGeofenceActive) "Disable" else "Enable",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Employees must be within the geofence radius to check in and check out.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Logout",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Are you sure you want to logout?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancel", color = PrimaryPurple)
                }
            }
        )
    }
}

@Composable
fun MockProfileSectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CrewHQCardBg),
        border = BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryPurple,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

@Composable
fun MockInfoField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondaryDark
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun MockInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondaryDark,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryDark
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

fun getInitials(name: String): String {
    val parts = name.trim().split("\\s+".toRegex())
    return when {
        parts.isEmpty() -> "AS"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> (parts[0].take(1) + parts[parts.size - 1].take(1)).uppercase()
    }
}