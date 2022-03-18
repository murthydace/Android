package com.local.growkart.api

import com.local.growkart.login.models.EmployeeResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("employees")
    suspend fun getEmployees(): Response<EmployeeResponse>
}