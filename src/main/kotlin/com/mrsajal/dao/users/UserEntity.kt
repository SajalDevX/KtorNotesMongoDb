package com.mrsajal.dao.users

import com.mrsajal.dao.notes.NotesEntity
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class UserEntity(
    @BsonId
    val userId: String = ObjectId().toString(),
    val name: String = "",
    val email:String="",
    val password: String = "",
    val imageUrl: String = "",
    val notes: List<NotesEntity> = listOf()
)