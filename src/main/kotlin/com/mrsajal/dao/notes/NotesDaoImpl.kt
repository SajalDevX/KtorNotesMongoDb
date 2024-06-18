package com.mrsajal.dao.notes

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mrsajal.dao.users.UserEntity
import org.bson.Document
import org.litote.kmongo.coroutine.CoroutineDatabase


class NotesDaoImpl(
    db: CoroutineDatabase
) : NotesDao {
    private val users = db.getCollection<UserEntity>("users")

    override suspend fun insertNotes(userId: String, notesEntity: NotesEntity): Boolean {
        val updatedUserNotes = users.updateOne(
            Filters.eq("_id", userId),
            Updates.addToSet("notes", notesEntity)
        )

        return updatedUserNotes.matchedCount > 0
    }

    override suspend fun editNote(userId: String, noteId: String, updatedNote: NotesEntity): Boolean {

        val filter = Filters.and(
            Filters.eq("_id", userId),
            Filters.eq("notes._id", noteId)
        )
        val update = Updates.combine(
            Updates.set("notes.$[note].noteTitle", updatedNote.noteTitle),
            Updates.set("notes.$[note].noteContent", updatedNote.noteContent),
            Updates.set("notes.$[note].createdAt", updatedNote.createdAt)
        )
        val updateOptions = UpdateOptions().arrayFilters(listOf(Document("note._id", noteId)))

        val updateResult = users.updateOne(filter, update, updateOptions)

        return updateResult.matchedCount > 0
    }


    override suspend fun getNoteById(userId: String, noteId: String): NotesEntity? {

        val userNote = users.findOne(Filters.eq("_id", userId))
        return userNote?.notes?.find { it.noteId == noteId }
    }

    override suspend fun deleteNote(userId: String, noteId: String): Boolean {

        val result = users.updateOne(
            Filters.eq("_id", userId),
            Updates.pull("notes", Document("_id", noteId))
        )
        return result.matchedCount > 0
    }

    override suspend fun getAllNotes(userId: String): List<NotesEntity> {

        val notes = users.findOne(Filters.eq("_id", userId))
        return notes?.notes ?: emptyList()
    }
}
