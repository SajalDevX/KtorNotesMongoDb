package com.mrsajal.dao.notes

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.jetbrains.exposed.sql.javatime.Date
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class NotesEntity(
    @BsonId
    val noteId: String = ObjectId().toString(),
    val noteTitle: String = "",
    val noteContent: String = "",
    val createdAt: Long = System.currentTimeMillis()
)