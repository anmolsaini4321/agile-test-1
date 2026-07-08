package com.example.smarthr_app.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.smarthr_app.data.model.Department
import com.example.smarthr_app.data.model.Gender
import com.example.smarthr_app.data.model.Position
import com.example.smarthr_app.data.model.UpdateProfileRequest
import com.example.smarthr_app.data.model.ExtraProfileDetails
import com.example.smarthr_app.presentation.theme.PrimaryPurple
import com.example.smarthr_app.presentation.viewmodel.AuthViewModel
import com.example.smarthr_app.utils.Resource
import com.example.smarthr_app.utils.ToastHelper
import com.example.smarthr_app.utils.ValidationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val user by authViewModel.user.collectAsState(initial = null)
    val extraDetails by authViewModel.extraProfileDetails.collectAsState(initial = null)
    val updateProfileState by authViewModel.updateProfileState.collectAsState(initial = null)

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf<Gender?>(null) }
    var selectedPosition by remember { mutableStateOf<Position?>(null) }
    var selectedDepartment by remember { mutableStateOf<Department?>(null) }

    // Mock/Extra details state variables
    var aadhar by remember { mutableStateOf("") }
    var maritalStatus by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var physicallyChallenged by remember { mutableStateOf("") }
    var currentAddress by remember { mutableStateOf("") }
    var permanentAddress by remember { mutableStateOf("") }
    var fathersName by remember { mutableStateOf("") }
    var mothersName by remember { mutableStateOf("") }
    var emergencyName by remember { mutableStateOf("") }
    var emergencyNumber by remember { mutableStateOf("") }
    var emergencyRelation by remember { mutableStateOf("") }
    var bankName by remember { mutableStateOf("") }
    var accountHolder by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }
    var ifscCode by remember { mutableStateOf("") }
    var upiId by remember { mutableStateOf("") }
    var uan by remember { mutableStateOf("") }
    var pan by remember { mutableStateOf("") }
    var pfNumber by remember { mutableStateOf("") }
    var pfJoining by remember { mutableStateOf("") }
    var esiNumber by remember { mutableStateOf("") }
    var esiJoining by remember { mutableStateOf("") }
    var epsNumber by remember { mutableStateOf("") }
    var epsExit by remember { mutableStateOf("") }

    var expandedGender by remember { mutableStateOf(false) }
    var expandedPosition by remember { mutableStateOf(false) }
    var expandedDepartment by remember { mutableStateOf(false) }

    // Initialize fields with current user data
    LaunchedEffect(user) {
        user?.let {
            name = it.name
            phone = it.phone?.replace("+91", "") ?: ""
            selectedGender = when (it.gender) {
                "M" -> Gender.M
                "F" -> Gender.F
                else -> null
            }
            selectedPosition = try {
                it.position?.let { pos -> Position.valueOf(pos) }
            } catch (e: Exception) { null }

            selectedDepartment = try {
                it.department?.let { dept -> Department.valueOf(dept) }
            } catch (e: Exception) { null }
        }
    }

    // Initialize fields with current extra details
    LaunchedEffect(extraDetails) {
        extraDetails?.let {
            aadhar = it.aadhar ?: ""
            maritalStatus = it.maritalStatus ?: ""
            bloodGroup = it.bloodGroup ?: ""
            physicallyChallenged = it.physicallyChallenged ?: ""
            currentAddress = it.currentAddress ?: ""
            permanentAddress = it.permanentAddress ?: ""
            fathersName = it.fathersName ?: ""
            mothersName = it.mothersName ?: ""
            emergencyName = it.emergencyName ?: ""
            emergencyNumber = it.emergencyNumber ?: ""
            emergencyRelation = it.emergencyRelation ?: ""
            bankName = it.bankName ?: ""
            accountHolder = it.accountHolder ?: ""
            accountNumber = it.accountNumber ?: ""
            ifscCode = it.ifscCode ?: ""
            upiId = it.upiId ?: ""
            uan = it.uan ?: ""
            pan = it.pan ?: ""
            pfNumber = it.pfNumber ?: ""
            pfJoining = it.pfJoining ?: ""
            esiNumber = it.esiNumber ?: ""
            esiJoining = it.esiJoining ?: ""
            epsNumber = it.epsNumber ?: ""
            epsExit = it.epsExit ?: ""
        }
    }

    // Handle update response
    LaunchedEffect(updateProfileState) {
        when (val state = updateProfileState) {
            is Resource.Success -> {
                ToastHelper.showSuccessToast(context, "Profile updated successfully!")
                authViewModel.clearUpdateProfileState()
                onNavigateBack()
            }
            is Resource.Error -> {
                ToastHelper.showErrorToast(context, state.message)
                authViewModel.clearUpdateProfileState()
            }
            else -> {}
        }
    }

    fun saveProfile() {
        val nameValidation = ValidationUtils.validateName(name)
        if (!nameValidation.isValid) {
            ToastHelper.showErrorToast(context, nameValidation.errorMessage)
            return
        }

        val cleanPhone = phone.filter { it.isDigit() }
        if (cleanPhone.isNotBlank()) {
            val phoneValidation = ValidationUtils.validatePhone(cleanPhone)
            if (!phoneValidation.isValid) {
                ToastHelper.showErrorToast(context, phoneValidation.errorMessage)
                return
            }
        }

        val formattedPhone = if (cleanPhone.isNotBlank()) {
            ValidationUtils.formatPhoneNumber(cleanPhone)
        } else null

        val updateRequest = UpdateProfileRequest(
            name = name.trim(),
            phone = formattedPhone,
            gender = selectedGender?.name,
            position = selectedPosition?.name,
            department = selectedDepartment?.name,
            aadhar = aadhar.trim(),
            maritalStatus = maritalStatus.trim(),
            bloodGroup = bloodGroup.trim(),
            physicallyChallenged = physicallyChallenged.trim(),
            currentAddress = currentAddress.trim(),
            permanentAddress = permanentAddress.trim(),
            fathersName = fathersName.trim(),
            mothersName = mothersName.trim(),
            emergencyName = emergencyName.trim(),
            emergencyNumber = emergencyNumber.trim(),
            emergencyRelation = emergencyRelation.trim(),
            bankName = bankName.trim(),
            accountHolder = accountHolder.trim(),
            accountNumber = accountNumber.trim(),
            ifscCode = ifscCode.trim(),
            upiId = upiId.trim(),
            uan = uan.trim(),
            pan = pan.trim(),
            pfNumber = pfNumber.trim(),
            pfJoining = pfJoining.trim(),
            esiNumber = esiNumber.trim(),
            esiJoining = esiJoining.trim(),
            epsNumber = epsNumber.trim(),
            epsExit = epsExit.trim()
        )

        // Save local extra details
        val extraRequest = com.example.smarthr_app.data.model.ExtraProfileDetails(
            aadhar = aadhar.trim(),
            maritalStatus = maritalStatus.trim(),
            bloodGroup = bloodGroup.trim(),
            physicallyChallenged = physicallyChallenged.trim(),
            currentAddress = currentAddress.trim(),
            permanentAddress = permanentAddress.trim(),
            fathersName = fathersName.trim(),
            mothersName = mothersName.trim(),
            emergencyName = emergencyName.trim(),
            emergencyNumber = emergencyNumber.trim(),
            emergencyRelation = emergencyRelation.trim(),
            bankName = bankName.trim(),
            accountHolder = accountHolder.trim(),
            accountNumber = accountNumber.trim(),
            ifscCode = ifscCode.trim(),
            upiId = upiId.trim(),
            uan = uan.trim(),
            pan = pan.trim(),
            pfNumber = pfNumber.trim(),
            pfJoining = pfJoining.trim(),
            esiNumber = esiNumber.trim(),
            esiJoining = esiJoining.trim(),
            epsNumber = epsNumber.trim(),
            epsExit = epsExit.trim()
        )

        authViewModel.saveExtraProfileDetails(extraRequest)
        authViewModel.updateProfile(updateRequest)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Top Bar
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
                        text = "Edit Profile",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Save Button
                TextButton(
                    onClick = { saveProfile() },
                    enabled = updateProfileState !is Resource.Loading
                ) {
                    if (updateProfileState is Resource.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "Save",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Form Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Basic Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Basic Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    // Name Field
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            focusedLabelColor = PrimaryPurple,
                            focusedLeadingIconColor = PrimaryPurple
                        )
                    )

                    // Phone Field
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { newValue ->
                            val digits = newValue.filter { it.isDigit() }
                            if (digits.length <= 10) {
                                phone = digits
                            }
                        },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null
                            )
                        },
                        prefix = { Text("+91 ") },
                        supportingText = { Text("10 digits only") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            focusedLabelColor = PrimaryPurple,
                            focusedLeadingIconColor = PrimaryPurple
                        )
                    )

                    // Gender Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedGender,
                        onExpandedChange = { expandedGender = !expandedGender }
                    ) {
                        OutlinedTextField(
                            value = when (selectedGender) {
                                Gender.M -> "Male"
                                Gender.F -> "Female"
                                null -> ""
                            },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Gender") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedGender
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryPurple,
                                focusedLabelColor = PrimaryPurple,
                                focusedLeadingIconColor = PrimaryPurple
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expandedGender,
                            onDismissRequest = { expandedGender = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Male") },
                                onClick = {
                                    selectedGender = Gender.M
                                    expandedGender = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Female") },
                                onClick = {
                                    selectedGender = Gender.F
                                    expandedGender = false
                                }
                            )
                        }
                    }

                    // Aadhar Field
                    OutlinedTextField(
                        value = aadhar,
                        onValueChange = { aadhar = it },
                        label = { Text("Aadhar Number") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            focusedLabelColor = PrimaryPurple
                        )
                    )
                }
            }

            // 2. Professional Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Professional Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    // Position Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedPosition,
                        onExpandedChange = { expandedPosition = !expandedPosition }
                    ) {
                        OutlinedTextField(
                            value = selectedPosition?.name?.replace("_", " ") ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Position") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedPosition
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Work,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                  focusedBorderColor = PrimaryPurple,
                                  focusedLabelColor = PrimaryPurple,
                                  focusedLeadingIconColor = PrimaryPurple
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expandedPosition,
                            onDismissRequest = { expandedPosition = false }
                        ) {
                            Position.values().forEach { position ->
                                DropdownMenuItem(
                                    text = { Text(position.name.replace("_", " ")) },
                                    onClick = {
                                        selectedPosition = position
                                        expandedPosition = false
                                    }
                                )
                            }
                        }
                    }

                    // Department Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedDepartment,
                        onExpandedChange = { expandedDepartment = !expandedDepartment }
                    ) {
                        OutlinedTextField(
                            value = selectedDepartment?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Department") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedDepartment
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryPurple,
                                focusedLabelColor = PrimaryPurple,
                                focusedLeadingIconColor = PrimaryPurple
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expandedDepartment,
                            onDismissRequest = { expandedDepartment = false }
                        ) {
                            Department.values().forEach { department ->
                                DropdownMenuItem(
                                    text = { Text(department.name) },
                                    onClick = {
                                        selectedDepartment = department
                                        expandedDepartment = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 3. Personal Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Personal Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    OutlinedTextField(
                        value = maritalStatus,
                        onValueChange = { maritalStatus = it },
                        label = { Text("Marital Status") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = bloodGroup,
                        onValueChange = { bloodGroup = it },
                        label = { Text("Blood Group") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = physicallyChallenged,
                        onValueChange = { physicallyChallenged = it },
                        label = { Text("Physically Challenged (Yes/No)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = currentAddress,
                        onValueChange = { currentAddress = it },
                        label = { Text("Current Address") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = permanentAddress,
                        onValueChange = { permanentAddress = it },
                        label = { Text("Permanent Address") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )
                }
            }

            // 4. Family Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Family Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    OutlinedTextField(
                        value = fathersName,
                        onValueChange = { fathersName = it },
                        label = { Text("Father's Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = mothersName,
                        onValueChange = { mothersName = it },
                        label = { Text("Mother's Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )
                }
            }

            // 5. Emergency Contact Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Emergency Contact",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    OutlinedTextField(
                        value = emergencyName,
                        onValueChange = { emergencyName = it },
                        label = { Text("Contact Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = emergencyNumber,
                        onValueChange = { emergencyNumber = it },
                        label = { Text("Contact Number") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = emergencyRelation,
                        onValueChange = { emergencyRelation = it },
                        label = { Text("Relation") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )
                }
            }

            // 6. Bank Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Bank Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    OutlinedTextField(
                        value = bankName,
                        onValueChange = { bankName = it },
                        label = { Text("Bank Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = accountHolder,
                        onValueChange = { accountHolder = it },
                        label = { Text("Account Holder Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = accountNumber,
                        onValueChange = { accountNumber = it },
                        label = { Text("Account Number") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = ifscCode,
                        onValueChange = { ifscCode = it },
                        label = { Text("IFSC Code") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = upiId,
                        onValueChange = { upiId = it },
                        label = { Text("UPI ID") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )
                }
            }

            // 7. Employment Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Employment Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )

                    OutlinedTextField(
                        value = uan,
                        onValueChange = { uan = it },
                        label = { Text("UAN") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = pan,
                        onValueChange = { pan = it },
                        label = { Text("PAN Number") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = pfNumber,
                        onValueChange = { pfNumber = it },
                        label = { Text("PF Number") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = pfJoining,
                        onValueChange = { pfJoining = it },
                        label = { Text("PF Joining Date") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = esiNumber,
                        onValueChange = { esiNumber = it },
                        label = { Text("ESI Number") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = esiJoining,
                        onValueChange = { esiJoining = it },
                        label = { Text("ESI Joining Date") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = epsNumber,
                        onValueChange = { epsNumber = it },
                        label = { Text("EPS Number") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )

                    OutlinedTextField(
                        value = epsExit,
                        onValueChange = { epsExit = it },
                        label = { Text("EPS Exit Date") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryPurple, focusedLabelColor = PrimaryPurple)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}