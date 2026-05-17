package com.example.checkin.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id") val id: Int,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("age") val age: Int,
    @SerializedName("image") val image: String,
    @SerializedName("company") val company: CompanyModel
)

data class CompanyModel(
    @SerializedName("name") val name: String,
    @SerializedName("title") val title: String,
    @SerializedName("department") val department: String
)

data class UserResponse(
    @SerializedName("users") val users: List<UserModel>
)