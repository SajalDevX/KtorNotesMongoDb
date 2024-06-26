package com.mrsajal.repository.notes

import com.mrsajal.dao.notes.NotesDao
import com.mrsajal.mappers.toNotesEntity
import com.mrsajal.model.EditNoteParams
import com.mrsajal.model.InsertNoteParams
import com.mrsajal.model.NoteResponse
import com.mrsajal.utils.Response
import io.ktor.http.*

class NotesRepositoryImpl(
    private val notesDao: NotesDao
) : NotesRepository {
    override suspend fun insertNote(params: InsertNoteParams): Response<NoteResponse> {
        val note = notesDao.insertNotes(params.userId, params.toNotesEntity())
        return if (!note) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = NoteResponse(
                    success = false,
                    message = "Note could not be created"
                )
            )
        } else {
            Response.Success(
                data = NoteResponse(
                    success = true,
                    message = "Note created successfully"
                )
            )
        }
    }

    override suspend fun editNote(params: EditNoteParams): Response<NoteResponse> {
        val edit = notesDao.editNote(params.userId, params.noteId, params.toNotesEntity())
        return if (!edit) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = NoteResponse(
                    success = false,
                    message = "Note could not be edited"
                )
            )
        } else {
            Response.Success(
                data = NoteResponse(
                    success = true,
                    message = "Note edited successfully"
                )
            )
        }
    }

    override suspend fun deleteNote(userId: String, noteId: String): Response<NoteResponse> {
        val deleted = notesDao.deleteNote(userId, noteId)
        return if (!deleted) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = NoteResponse(
                    success = false,
                    message = "Note could not be deleted"
                )
            )
        } else {
            Response.Success(
                data = NoteResponse(
                    success = true,
                    message = "Note deleted successfully"
                )
            )
        }
    }


    override suspend fun getUserNotes(userId: String, pageNumber: Int, pageSize: Int): Response<NoteResponse> {
        val listOfNotes = notesDao.getAllNotes(userId, pageNumber, pageSize)
        return if (listOfNotes.isEmpty()) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = NoteResponse(
                    success = false,
                    message = "No notes available, click to add notes"
                )
            )
        } else {
            Response.Success(
                data = NoteResponse(
                    success = true,
                    notes = listOfNotes,
                    message = "Notes fetched successfully"
                )
            )
        }
    }

    override suspend fun getNoteById(userId: String, noteId: String): Response<NoteResponse> {
        val note = notesDao.getNoteById(userId, noteId)
        return if (note == null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = NoteResponse(
                    success = false,
                    message = "Note unavailable"
                )
            )
        } else {
            Response.Success(
                data = NoteResponse(
                    success = true,
                    note = note,
                    message = "Note fetched successfully"
                )
            )
        }
    }
}