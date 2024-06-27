package com.mrsajal.routing

import com.mrsajal.model.EditNoteParams
import com.mrsajal.model.GetNotesParams
import com.mrsajal.model.InsertNoteParams
import com.mrsajal.model.NoteResponse
import com.mrsajal.repository.notes.NotesRepository
import com.mrsajal.utils.getStringParameter
import io.ktor.client.engine.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ContentTransformationException
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
            delete("/delete") {
                try {
                    val params = call.receiveNullable<GetNotesParams>()
                    if (params == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = NoteResponse(
                                success = false,
                                message = "Could not parse notes parameters"
                            )
                        )
                        return@delete
                    }
                    val result = repository.deleteNote(userId = params.userId, noteId = params.noteId)

                    call.respond(
                        status = result.code,
                        message = result.data
                    )
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
            get("/fetch") {
                try {
                    val userId = call.parameters["userId"]
                    val noteId = call.parameters["noteId"]
                    if (noteId == null || userId == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = NoteResponse(
                                success = false,
                                message = "Could not parse notes parameters"
                            )
                        )
                        return@get
                    }
                    val result = repository.getNoteById(userId, noteId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (badRequestError: BadRequestException) {
                    return@get
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request format")
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
            route("/all") {
                get("/{userId}") {
                    try {
                        val userId = call.parameters["userId"]!!
                        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10

                        val result = repository.getUserNotes(userId, page, limit)
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
}