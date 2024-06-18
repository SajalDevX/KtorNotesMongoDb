package com.mrsajal.routing

import com.mrsajal.model.EditNoteParams
import com.mrsajal.model.InsertNoteParams
import com.mrsajal.model.NoteResponse
import com.mrsajal.repository.notes.NotesRepository
import com.mrsajal.utils.getStringParameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.notesRouting() {

    val repository by inject<NotesRepository>()

    authenticate {
        route(path = "/note") {
            post("/create") {
                try {
                    val params = call.receiveNullable<InsertNoteParams>()

                    if (params == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = NoteResponse(
                                success = false,
                                message = "Could not parse notes parameters"
                            )
                        )
                        return@post
                    }

                    val result = repository.insertNote(params)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )

                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = NoteResponse(
                            success = false,
                            message = "An unexpected error occurred, try again!"
                        )
                    )
                    return@post
                }
            }
            post("/edit") {
                try {
                    val params = call.receiveNullable<EditNoteParams>()

                    if (params == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = NoteResponse(
                                success = false,
                                message = "Could not parse notes parameters"
                            )
                        )
                        return@post
                    }

                    val result = repository.editNote(params)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )

                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = NoteResponse(
                            success = false,
                            message = "An unexpected error occurred, try again!"
                        )
                    )
                    return@post
                }
            }
            delete("/{noteId}") {
                try {
                    val noteId = call.getStringParameter(name = "noteId")
                    val userId = call.getStringParameter(name = "userId")
                    val result = repository.deleteNote(userId = userId, noteId = noteId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (badRequestError: BadRequestException) {
                    return@delete
                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = NoteResponse(
                            success = false,
                            message = "An unexpected error has occurred, try again!"
                        )
                    )
                }
            }
            get("/{noteId}") {
                try {
                    val noteId = call.getStringParameter("noteId")
                    val userId = call.getStringParameter("userId")

                    val result = repository.getNoteById(userId, noteId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (badRequestError: BadRequestException) {
                    return@get
                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = NoteResponse(
                            success = false,
                            message = "An unexpected error has occurred, try again!"
                        )
                    )
                }
            }
        }
        route("/notes") {
            get("/{userId}") {
                try {
                    val userId = call.parameters["userId"]!!
                    val result = repository.getUserNotes(userId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (badRequestError: BadRequestException) {
                    return@get
                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = NoteResponse(
                            success = false,
                            message = "An unexpected error has occurred, try again!"
                        )
                    )
                }
            }
        }
    }
}