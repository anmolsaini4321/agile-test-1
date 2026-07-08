package com.example.smarthr_app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String? = "",
    val role: UserRole = UserRole.ROLE_USER,
    val companyCode: String? = null,
    val imageUrl: String? = null,
    val createdAt: String = "",
    val gender: String? = null,
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
) : Parcelable

enum class UserRole {
    ROLE_HR, ROLE_USER
}