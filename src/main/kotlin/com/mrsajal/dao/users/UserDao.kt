package com.mrsajal.dao.users

interface UserDao {
    suspend fun insertUser(userEntity: UserEntity): UserEntity?
    suspend fun findUserByEmail(email: String): UserEntity?
}