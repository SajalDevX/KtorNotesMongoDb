package com.mrsajal.repository.auth

import com.mrsajal.dao.users.UserDao
import com.mrsajal.mappers.toUserEntity
import com.mrsajal.model.AuthResponse
import com.mrsajal.model.AuthResponseData
import com.mrsajal.model.SignInParams
import com.mrsajal.model.SignUpParams
import com.mrsajal.plugins.generateToken
import com.mrsajal.security.hashPassword
import com.mrsajal.utils.Response
import io.ktor.http.*

class AuthRepositoryImpl(
    private val userDao:UserDao
) : AuthRepository {
    override suspend fun signUp(params: SignUpParams): Response<AuthResponse> {
        return if (userAlreadyExists(params.email)) {
            Response.Error(
                code = HttpStatusCode.Conflict,
                data = AuthResponse(
                    errorMessage = "User already exists, please try logging in"
                )
            )
        } else {
            val insertedUser = userDao.insertUser(params.toUserEntity())
            if (insertedUser == null) {
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = AuthResponse(
                        errorMessage = "Oops, sorry we couldn't register the user try later"
                    )
                )
            }else {
                Response.Success(
                    data = AuthResponse(
                        AuthResponseData(
                            userId = insertedUser.userId,
                            name = insertedUser.name,
                            imageUrl = insertedUser.imageUrl,
                            token = generateToken(params.email)
                        )
                    )
                )
            }
        }
    }

    override suspend fun signIn(params: SignInParams): Response<AuthResponse> {
        val user = userDao.findUserByEmail(params.email)
        return if(user==null){
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = AuthResponse(
                    errorMessage = "Invalid Credentials, No user with this email!"
                )
            )
        }
        else{
            val hashedPassword = hashPassword(params.password)
            if (user.password == hashedPassword) {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            userId = user.userId,
                            name = user.name,
                            imageUrl = user.imageUrl,
                            token = generateToken(params.email)
                        )
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.Forbidden,
                    data = AuthResponse(
                        errorMessage = "Invalid Credentials, Wrong password! $hashedPassword != ${params.password}"
                    )
                )
            }
        }
    }
    private suspend fun userAlreadyExists(email: String): Boolean {
        return userDao.findUserByEmail(email) != null
    }
}