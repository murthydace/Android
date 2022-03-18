package com.local.growkart.api

import com.local.growkart.login.models.EmployeeResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun getEmployees(): Response<EmployeeResponse> = apiService.getEmployees()
}