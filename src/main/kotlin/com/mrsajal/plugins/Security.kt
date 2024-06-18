package com.mrsajal.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mrsajal.model.AuthResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.util.*


val jwtAudience = System.getenv("jwt.audience")
val jwtIssuer = System.getenv("jwt.issuer")
val jwtSecret = System.getenv("jwt.secret")
private const val CLAIM = "email"

fun Application.configureSecurity() {
    authentication {
        jwt {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim(CLAIM).asString() != null) {
                    JWTPrincipal(payload = credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = AuthResponse(
                        errorMessage = "Token is not valid or expired"
                    )
                )
            }
        }
    }
}

fun generateToken(email: String): String {
//    val longExpireAfter1Hr = System.currentTimeMillis() + 36_00_00

    return JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM, email)
//        .withExpiresAt(Date(longExpireAfter1Hr))
        .sign(Algorithm.HMAC256(jwtSecret))

}