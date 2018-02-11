package io.github.johnfg10

data class ErrorResponse(val errorText: String) : ResponseBase(RequestType.Error)