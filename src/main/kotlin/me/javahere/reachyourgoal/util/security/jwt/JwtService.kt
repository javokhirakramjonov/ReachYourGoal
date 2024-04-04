package me.javahere.reachyourgoal.util.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import me.javahere.reachyourgoal.util.extensions.EMPTY
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

@Service
class JwtService(
    @Value("\${app.access-secret}") private val accessSecret: String,
    @Value("\${app.refresh-secret}") private val refreshSecret: String,
) {
    companion object {
        const val JWT_KEY_PAIR_CONFIRM_REGISTER = "confirmRegister"
        const val JWT_KEY_PAIR_CONFIRM_NEW_EMAIL = "confirmNewEmail"
        const val JWT_EXTRA_KEY_1 = "key1"
        const val JWT_EXTRA_KEY_2 = "key2"
        const val TOKEN_PREFIX = "Bearer "
        const val ROLES_KEY = "roles"
        val EXPIRE_ACCESS_TOKEN = 1.days
        val EXPIRE_REFRESH_TOKEN = 30.days
        val EXPIRE_CONFIRMATION_TOKEN = 5.minutes
    }

    fun generateAccessToken(
        userId: String,
        username: String,
        expiryDuration: Duration,
        roles: Array<String>,
        key1: String = String.EMPTY,
        key2: String = String.EMPTY,
    ): String = generate(userId, username, expiryDuration, roles, accessSecret, key1, key2)

    fun generateRefreshToken(
        userId: String,
        username: String,
        expiryDuration: Duration,
        roles: Array<String>,
    ): String = generate(userId, username, expiryDuration, roles, refreshSecret)

    fun decodeAccessToken(accessToken: String): DecodedJWT = decode(accessSecret, accessToken)

    fun decodeRefreshToken(refreshToken: String): DecodedJWT = decode(refreshSecret, refreshToken)

    fun refreshAccessToken(refreshToken: String): String {
        val decodedJWT = decodeRefreshToken(refreshToken)

        val roles = getRoles(decodedJWT)

        val accessToken =
            generateAccessToken(
                decodedJWT.issuer,
                decodedJWT.subject,
                EXPIRE_ACCESS_TOKEN,
                roles,
            )

        return accessToken
    }

    fun getRoles(decodedJWT: DecodedJWT): Array<String> {
        return decodedJWT
            .getClaim(ROLES_KEY)
            .asList(String::class.java)
            .toTypedArray()
    }

    private fun generate(
        issuer: String,
        subject: String,
        expiryDuration: Duration,
        roles: Array<String>,
        signature: String,
        key1: String = String.EMPTY,
        key2: String = String.EMPTY,
    ): String =
        JWT
            .create()
            .withIssuer(issuer)
            .withSubject(subject)
            .withClaim(JWT_EXTRA_KEY_1, key1)
            .withClaim(JWT_EXTRA_KEY_2, key2)
            .withExpiresAt(Date(System.currentTimeMillis() + expiryDuration.inWholeMilliseconds))
            .withArrayClaim(ROLES_KEY, roles)
            .sign(Algorithm.HMAC512(signature.toByteArray()))

    private fun decode(
        signature: String,
        token: String,
    ): DecodedJWT =
        JWT
            .require(Algorithm.HMAC512(signature.toByteArray()))
            .build()
            .verify(token.replace(TOKEN_PREFIX, String.EMPTY))
}
