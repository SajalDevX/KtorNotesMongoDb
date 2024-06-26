package com.mrsajal.dao.notes

interface NotesDao {
    suspend fun insertNotes(userId: String, notesEntity: NotesEntity): Boolean
    suspend fun editNote(userId: String, noteId: String, updatedNote: NotesEntity): Boolean
    suspend fun getNoteById(userId: String, noteId: String): NotesEntity?
    suspend fun deleteNote(userId: String, noteId: String): Boolean
    suspend fun getAllNotes(userId: String, pageNumber: Int, pageSize: Int): List<NotesEntity>
}