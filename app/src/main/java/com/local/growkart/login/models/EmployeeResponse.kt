package com.local.growkart.login.models

data class EmployeeResponse(
    val data: List<Employee>?=null,
    val status: String?=""
)