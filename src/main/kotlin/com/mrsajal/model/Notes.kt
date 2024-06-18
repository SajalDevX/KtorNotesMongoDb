package com.mrsajal.model

import com.mrsajal.dao.notes.NotesEntity
import kotlinx.serialization.Serializable

@Serializable
data class InsertNoteParams(
    val userId: String,
    val noteTitle: String,
    val noteContent: String
)

@Serializable
data class EditNoteParams(
    val noteId: String,
    val userId: String,
    val noteTitle: String,
    val noteContent: String
)

@Serializable
data class NoteResponse(
    val success: Boolean,
    val note: NotesEntity? = null,
    val notes: List<NotesEntity> = listOf(),
    val message: String? = null
)