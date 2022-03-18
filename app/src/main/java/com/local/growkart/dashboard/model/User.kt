package com.local.growkart.dashboard.model

data class User(
    var custId: String = "",
    var name: String? = "",
    var phoneNumber: String? = "",
    var gender: String? = "",
    var dob: String = "",
    var address: Address = Address()
)

data class Address(
    var plotNo: String = "",
    var street: String = "",
    var postCode: String = "",
    var country: String = "",
    var town: String = ""
)
