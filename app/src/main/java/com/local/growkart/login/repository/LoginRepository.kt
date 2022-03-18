package com.local.growkart.login.repository

import com.local.growkart.api.ApiHelper
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    suspend fun getEmployee() = apiHelper.getEmployees()
}