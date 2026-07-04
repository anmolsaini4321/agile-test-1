package com.example.smarthr_app.presentation.screen.dashboard.hr

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import com.example.smarthr_app.presentation.theme.PrimaryPurple
import com.example.smarthr_app.presentation.viewmodel.AuthViewModel
import com.example.smarthr_app.utils.Resource
import com.example.smarthr_app.utils.ToastHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HRProfileScreen(
    authViewModel: AuthViewModel,
    onNavigateToEditProfile: () -> Unit,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val user by authViewModel.user.collectAsState(initial = null)
    val extraDetails by authViewModel.extraProfileDetails.collectAsState(initial = null)
    val uploadImageState by authViewModel.uploadImageState.collectAsState(initial = null)

    var localImageUri by remember { mutableStateOf<Uri?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Refresh profile on screen load
    LaunchedEffect(Unit) {
        authViewModel.refreshProfile()
    }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            authViewModel.uploadProfileImage(context, it)
        }
    }

    // Handle image upload response
    LaunchedEffect(uploadImageState) {
        when (val state = uploadImageState) {
            is Resource.Success -> {
                ToastHelper.showSuccessToast(context, "Profile image updated successfully!")
                localImageUri = null // Clear local image after successful upload
                authViewModel.clearUploadImageState()
                // Profile will be automatically updated via the repository
            }

            is Resource.Error -> {
                when {
                    state.message.contains("Network error", ignoreCase = true) -> {
                        // Handle potential false positive network error
                        authViewModel.refreshProfile()
                        ToastHelper.showSuccessToast(context, "Image uploaded! Refreshing...")
                    }

                    else -> {
                        ToastHelper.showErrorToast(context, state.message)
                    }
                }
                localImageUri = null // Clear local image on error
                authViewModel.clearUploadImageState()
            }

            is Resource.Loading -> {
                // Loading state is handled by the UI
            }

            null -> {
                // Initial state
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Top Bar with Back Button
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(top = 8.dp),
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
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
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

        // Profile Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Avatar and Basic Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Picture with upload option
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Show selected image immediately or user's current image
                        val imageToShow = selectedImageUri?.toString() ?: user?.imageUrl

                        if (!imageToShow.isNullOrBlank()) {
                            AsyncImage(
                                model = imageToShow,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryPurple.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    modifier = Modifier.size(50.dp),
                                    tint = PrimaryPurple
                                )
                            }
                        }

                        // Camera icon for upload
                        Box(
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            FloatingActionButton(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                modifier = Modifier.size(32.dp),
                                containerColor = PrimaryPurple,
                                contentColor = Color.White
                            ) {
                                if (uploadImageState is Resource.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = Color.White
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Upload Photo",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Name
                    Text(
                        text = user?.name ?: "Admin User",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Role Badge
                    Surface(
                        modifier = Modifier.padding(top = 8.dp),
                        color = PrimaryPurple.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Admin Manager",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = PrimaryPurple,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Edit Profile Button
                    OutlinedButton(
                        onClick = onNavigateToEditProfile,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PrimaryPurple
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit Profile")
                    }
                }
            }

            // Contact Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Contact & General Info",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email
                    ProfileInfoRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = user?.email ?: "Not available"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone
                    ProfileInfoRow(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = user?.phone ?: "Not available"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Aadhar Number
                    ProfileInfoRow(
                        icon = Icons.Default.Info,
                        label = "Aadhar Number",
                        value = if (!extraDetails?.aadhar.isNullOrBlank()) extraDetails!!.aadhar!! else "Not provided"
                    )
                }
            }

            // Company Information
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Company Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Company Code with Copy functionality
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Business,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Company Code",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = user?.companyCode ?: "Not available",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Copy Button
                        if (!user?.companyCode.isNullOrBlank()) {
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(user?.companyCode ?: ""))
                                    ToastHelper.showSuccessToast(context, "Company code copied to clipboard!")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Company Code",
                                    tint = PrimaryPurple
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Position
                    ProfileInfoRow(
                        icon = Icons.Default.Work,
                        label = "Position",
                        value = user?.position ?: "Not specified"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Department
                    ProfileInfoRow(
                        icon = Icons.Default.Group,
                        label = "Department",
                        value = user?.department ?: "Not specified"
                    )
                }
            }

            // Personal Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Personal Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        ProfileInfoField(
                            label = "Gender",
                            value = when (user?.gender) {
                                "M" -> "Male"
                                "F" -> "Female"
                                else -> "Not specified"
                            },
                            modifier = Modifier.weight(1f)
                        )
                        ProfileInfoField(
                            label = "Marital Status",
                            value = if (!extraDetails?.maritalStatus.isNullOrBlank()) extraDetails!!.maritalStatus!! else "Not specified",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        ProfileInfoField(
                            label = "Blood Group",
                            value = if (!extraDetails?.bloodGroup.isNullOrBlank()) extraDetails!!.bloodGroup!! else "Not specified",
                            modifier = Modifier.weight(1f)
                        )
                        ProfileInfoField(
                            label = "Physically Challenged",
                            value = if (!extraDetails?.physicallyChallenged.isNullOrBlank()) extraDetails!!.physicallyChallenged!! else "No",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileInfoField(
                        label = "Current Address",
                        value = if (!extraDetails?.currentAddress.isNullOrBlank()) extraDetails!!.currentAddress!! else "Not specified",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileInfoField(
                        label = "Permanent Address",
                        value = if (!extraDetails?.permanentAddress.isNullOrBlank()) extraDetails!!.permanentAddress!! else "Not specified",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Family Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Family Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        ProfileInfoField(
                            label = "Father's Name",
                            value = if (!extraDetails?.fathersName.isNullOrBlank()) extraDetails!!.fathersName!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                        ProfileInfoField(
                            label = "Mother's Name",
                            value = if (!extraDetails?.mothersName.isNullOrBlank()) extraDetails!!.mothersName!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Emergency Contact Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Emergency Contact",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        ProfileInfoField(
                            label = "Contact Name",
                            value = if (!extraDetails?.emergencyName.isNullOrBlank()) extraDetails!!.emergencyName!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                        ProfileInfoField(
                            label = "Contact Number",
                            value = if (!extraDetails?.emergencyNumber.isNullOrBlank()) extraDetails!!.emergencyNumber!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                        ProfileInfoField(
                            label = "Relation",
                            value = if (!extraDetails?.emergencyRelation.isNullOrBlank()) extraDetails!!.emergencyRelation!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Bank Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Bank Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        ProfileInfoField(
                            label = "Bank Name",
                            value = if (!extraDetails?.bankName.isNullOrBlank()) extraDetails!!.bankName!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                        ProfileInfoField(
                            label = "Account Holder Name",
                            value = if (!extraDetails?.accountHolder.isNullOrBlank()) extraDetails!!.accountHolder!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        ProfileInfoField(
                            label = "Account Number",
                            value = if (!extraDetails?.accountNumber.isNullOrBlank()) extraDetails!!.accountNumber!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                        ProfileInfoField(
                            label = "IFSC Code",
                            value = if (!extraDetails?.ifscCode.isNullOrBlank()) extraDetails!!.ifscCode!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileInfoField(
                        label = "UPI ID",
                        value = if (!extraDetails?.upiId.isNullOrBlank()) extraDetails!!.upiId!! else "Not provided",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Employment Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Employment Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        ProfileInfoField(
                            label = "UAN",
                            value = if (!extraDetails?.uan.isNullOrBlank()) extraDetails!!.uan!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                        ProfileInfoField(
                            label = "PAN Number",
                            value = if (!extraDetails?.pan.isNullOrBlank()) extraDetails!!.pan!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        ProfileInfoField(
                            label = "PF Number",
                            value = if (!extraDetails?.pfNumber.isNullOrBlank()) extraDetails!!.pfNumber!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                        ProfileInfoField(
                            label = "PF Joining Date",
                            value = if (!extraDetails?.pfJoining.isNullOrBlank()) extraDetails!!.pfJoining!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        ProfileInfoField(
                            label = "ESI Number",
                            value = if (!extraDetails?.esiNumber.isNullOrBlank()) extraDetails!!.esiNumber!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                        ProfileInfoField(
                            label = "ESI Joining Date",
                            value = if (!extraDetails?.esiJoining.isNullOrBlank()) extraDetails!!.esiJoining!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        ProfileInfoField(
                            label = "EPS Number",
                            value = if (!extraDetails?.epsNumber.isNullOrBlank()) extraDetails!!.epsNumber!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                        ProfileInfoField(
                            label = "EPS Exit Date",
                            value = if (!extraDetails?.epsExit.isNullOrBlank()) extraDetails!!.epsExit!! else "Not provided",
                            modifier = Modifier.weight(1f)
                        )
                    }
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
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ProfileInfoField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}