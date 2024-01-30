package me.javahere.reachyourgoal.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import me.javahere.reachyourgoal.util.EMPTY
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.Date

@Service
class JwtService(
    @Value("\${app.access-secret}") private val accessSecret: String,
    @Value("\${app.refresh-secret}") private val refreshSecret: String,
) {
    companion object {
        const val JWT_EXTRA_KEY = "key"
        const val TOKEN_PREFIX = "Bearer "
        const val ROLES_KEY = "roles"
        val EXPIRE_ACCESS_TOKEN = Duration.ofDays(1).toMillis()
        val EXPIRE_REFRESH_TOKEN = Duration.ofDays(30).toMillis()
        val EXPIRE_CONFIRMATION_TOKEN = Duration.ofMinutes(5).toMillis()
    }

    fun generateAccessToken(
        userId: String,
        username: String,
        expirationInMillis: Long,
        roles: Array<String>,
        key: String = String.EMPTY,
    ): String = generate(userId, username, expirationInMillis, roles, accessSecret, key)

    fun generateRefreshToken(
        userId: String,
        username: String,
        expirationInMillis: Long,
        roles: Array<String>,
        key: String = String.EMPTY,
    ): String = generate(userId, username, expirationInMillis, roles, refreshSecret, key)

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
        expirationInMillis: Long,
        roles: Array<String>,
        signature: String,
        key: String,
    ): String =
        JWT
            .create()
            .withIssuer(issuer)
            .withSubject(subject)
            .withClaim(JWT_EXTRA_KEY, key)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationInMillis))
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
