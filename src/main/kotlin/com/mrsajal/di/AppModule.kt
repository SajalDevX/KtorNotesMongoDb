package com.mrsajal.di

import com.mrsajal.dao.notes.NotesDao
import com.mrsajal.dao.notes.NotesDaoImpl
import com.mrsajal.dao.users.UserDao
import com.mrsajal.dao.users.UserDaoImpl
import com.mrsajal.repository.auth.AuthRepository
import com.mrsajal.repository.auth.AuthRepositoryImpl
import com.mrsajal.repository.notes.NotesRepository
import com.mrsajal.repository.notes.NotesRepositoryImpl
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import kotlin.math.sin

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl(get()) }
    single<NotesDao> { NotesDaoImpl(get()) }
    single<NotesRepository> { NotesRepositoryImpl(get()) }




    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("notesDb")
    }
}
