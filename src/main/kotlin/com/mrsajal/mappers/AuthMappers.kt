package com.mrsajal.mappers

import com.mrsajal.dao.users.UserEntity
import com.mrsajal.model.SignUpParams
import com.mrsajal.security.hashPassword

fun SignUpParams.toUserEntity() =
    UserEntity(
        name = name,
        email = email,
        password = hashPassword(password)
    )