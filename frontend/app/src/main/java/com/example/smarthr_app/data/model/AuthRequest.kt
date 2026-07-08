package com.example.smarthr_app.data.model

import com.google.gson.annotations.SerializedName

data class UserRegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val gender: String = "M",
    val role: String,
    val companyCode: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class GoogleLoginRequest(
    val idToken: String
)

data class GoogleSignUpRequest(
    val idToken :String,
    val role:String
)

data class AuthResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    @SerializedName("id")
    val userId: String,
    val name: String,
    val email: String,
    val phone: String,
    val gender: String?,
    val role: String,
    val companyCode: String?,
    val imageUrl: String?,
    val position: String? = null,
    val department: String? = null,
    val waitingCompanyCode: String? = null,
    val joiningStatus: String? = null,
    val aadhar: String? = null,
    val maritalStatus: String? = null,
    val bloodGroup: String? = null,
    val physicallyChallenged: String? = null,
    val currentAddress: String? = null,
    val permanentAddress: String? = null,
    val fathersName: String? = null,
    val mothersName: String? = null,
    val emergencyName: String? = null,
    val emergencyNumber: String? = null,
    val emergencyRelation: String? = null,
    val bankName: String? = null,
    val accountHolder: String? = null,
    val accountNumber: String? = null,
    val ifscCode: String? = null,
    val upiId: String? = null,
    val uan: String? = null,
    val pan: String? = null,
    val pfNumber: String? = null,
    val pfJoining: String? = null,
    val esiNumber: String? = null,
    val esiJoining: String? = null,
    val epsNumber: String? = null,
    val epsExit: String? = null
)

data class UpdateProfileRequest(
    val name: String,
    val phone: String?,
    val gender: String?,
    val position: String?,
    val department: String?,
    val aadhar: String? = null,
    val maritalStatus: String? = null,
    val bloodGroup: String? = null,
    val physicallyChallenged: String? = null,
    val currentAddress: String? = null,
    val permanentAddress: String? = null,
    val fathersName: String? = null,
    val mothersName: String? = null,
    val emergencyName: String? = null,
    val emergencyNumber: String? = null,
    val emergencyRelation: String? = null,
    val bankName: String? = null,
    val accountHolder: String? = null,
    val accountNumber: String? = null,
    val ifscCode: String? = null,
    val upiId: String? = null,
    val uan: String? = null,
    val pan: String? = null,
    val pfNumber: String? = null,
    val pfJoining: String? = null,
    val esiNumber: String? = null,
    val esiJoining: String? = null,
    val epsNumber: String? = null,
    val epsExit: String? = null
)

data class CompanyWaitlistResponse(
    val companyCode: String,
    val users: List<UserDto>
)

data class CompanyEmployeesResponse(
    val companyCode: String,
    val users: List<UserDto>
)

data class SuccessApiResponseMessage(
    val message: String
)

data class UploadImageResponse(
    val message: String
)

// Enums for dropdowns
enum class Position {
    INTERN, JUNIOR_DEVELOPER, SENIOR_DEVELOPER, TEAM_LEAD, MANAGER, HR, CTO, CEO, OTHERS
}

enum class Department {
    HR, ENGINEERING, SALES, MARKETING, FINANCE, OPERATIONS, ADMINISTRATION, SUPPORT, OTHERS
}

enum class Gender {
    M, F
}

