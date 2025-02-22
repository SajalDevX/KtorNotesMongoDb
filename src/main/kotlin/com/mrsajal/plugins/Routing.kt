package com.mrsajal.plugins

import com.mrsajal.routing.authRouting
import com.mrsajal.routing.notesRouting
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRouting()
        notesRouting()
    }
}
