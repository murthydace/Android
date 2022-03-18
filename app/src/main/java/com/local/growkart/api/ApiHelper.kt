package com.local.growkart.api

import com.local.growkart.login.models.EmployeeResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun getEmployees():Response<EmployeeResponse>
}