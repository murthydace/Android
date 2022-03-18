package com.local.growkart.util

sealed class Error(val error: String?, errorType: ErrorType) {
    object FETCH_FAILURE :
        Error(
            "Unable to fetch the user details at the moment. Please try again later!",
            ErrorType.ERROR
        )

    object UPDATE_FAILURE :
        Error("Unable to update the user details. Try again later!", ErrorType.ERROR)

    object UNKNOWN_ERROR : Error("Something went wrong. Please try again later!", ErrorType.ERROR)
}

sealed class ErrorType {
    object INFO : ErrorType()
    object ERROR : ErrorType()
    class ERRORWITHCTA(cta: String, action: Action) : ErrorType()

}