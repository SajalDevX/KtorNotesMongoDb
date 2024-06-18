package com.mrsajal.mappers

import com.mrsajal.dao.notes.NotesEntity
import com.mrsajal.model.EditNoteParams
import com.mrsajal.model.InsertNoteParams

fun InsertNoteParams.toNotesEntity() =
    NotesEntity(
        noteTitle = noteTitle,
        noteContent = noteContent
    )

fun EditNoteParams.toNotesEntity() =
    NotesEntity(
        noteTitle = noteTitle,
        noteContent = noteContent
    )