package com.mrsajal.repository.notes

import com.mrsajal.model.EditNoteParams
import com.mrsajal.model.InsertNoteParams
import com.mrsajal.model.NoteResponse
import com.mrsajal.utils.Response

interface NotesRepository {
    suspend fun insertNote(params: InsertNoteParams):Response<NoteResponse>
    suspend fun editNote(params: EditNoteParams):Response<NoteResponse>
    suspend fun deleteNote(userId: String,noteId:String):Response<NoteResponse>
    suspend fun getUserNotes(userId: String):Response<NoteResponse>
    suspend fun getNoteById(userId: String,noteId:String): Response<NoteResponse>
}