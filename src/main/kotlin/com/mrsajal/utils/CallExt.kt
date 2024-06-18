package com.mrsajal.utils

import com.mrsajal.model.NoteResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*

suspend fun ApplicationCall.getStringParameter(name: String, isQueryParameter: Boolean = false): String{
    val parameter = if (isQueryParameter){
        request.queryParameters[name]
    }else{
        parameters[name]
    } ?: kotlin.run {
        respond(
            status = HttpStatusCode.BadRequest,
            message = NoteResponse(
                success = false,
                message = "Parameter $name is missing or invalid"
            )
        )
        throw BadRequestException("Parameter $name is missing or invalid")
    }
    return parameter
}