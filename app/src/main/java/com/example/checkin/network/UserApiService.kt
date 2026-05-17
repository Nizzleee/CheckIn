package com.example.checkin.network

import com.example.checkin.model.UserModel
import com.example.checkin.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    @GET("users?limit=20")
    suspend fun getUsers(): UserResponse

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserModel

    @GET("users/search")
    suspend fun searchUsers(@Query("q") query: String): UserResponse
}