package me.javahere.reachyourgoal.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import me.javahere.reachyourgoal.exception.*
import me.javahere.reachyourgoal.util.EMPTY
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service
class JwtService(
    @Value("\${app.access-secret}") private val accessSecret: String,
    @Value("\${app.refresh-secret}") private val refreshSecret: String
) {

    companion object {
        val EXPIRE_ACCESS_TOKEN = Duration.ofSeconds(1).toMillis()
        val EXPIRE_REFRESH_TOKEN = Duration.ofDays(30).toMillis()
        val EXPIRE_CONFIRMATION_TOKEN = Duration.ofMinutes(5).toMillis()
    }

    fun generateAccessToken(username: String, expirationInMillis: Long, roles: Array<String>): String =
        generate(username, expirationInMillis, roles, accessSecret)

    fun generateRefreshToken(username: String, expirationInMillis: Long, roles: Array<String>): String =
        generate(username, expirationInMillis, roles, refreshSecret)

    fun decodeAccessToken(accessToken: String): DecodedJWT? = decode(accessSecret, accessToken)

    fun decodeRefreshToken(refreshToken: String): DecodedJWT? = decode(refreshSecret, refreshToken)

    fun refreshAccessToken(refreshToken: String): String {
        val invalidRefreshToken =
            ExceptionResponse(ReachYourGoalException(ReachYourGoalExceptionType.INVALID_REFRESH_TOKEN))
        val refreshTokenExpired =
            ExceptionResponse(ReachYourGoalException(ReachYourGoalExceptionType.REFRESH_TOKEN_EXPIRED))

        val decodedJWT = decodeRefreshToken(refreshToken) ?: throw invalidRefreshToken

        if (!isValidExpireDate(decodedJWT)) throw refreshTokenExpired

        val username = decodedJWT.subject
        val roles = decodedJWT
            .getClaim("role")
            .asList(String::class.java)
            .toTypedArray()

        val accessToken = generateAccessToken(
            username,
            EXPIRE_ACCESS_TOKEN,
            roles
        )

        return accessToken
    }

    fun isValidExpireDate(decodedJWT: DecodedJWT): Boolean {
        val expireTime = decodedJWT.expiresAt.time
        val currentTime = System.currentTimeMillis()

        return expireTime >= currentTime
    }

    private fun generate(username: String, expirationInMillis: Long, roles: Array<String>, signature: String): String =
        JWT.create()
            .withSubject(username)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationInMillis))
            .withArrayClaim("role", roles)
            .sign(Algorithm.HMAC512(signature.toByteArray()))

    private fun decode(signature: String, token: String): DecodedJWT? = runCatching {
        JWT.require(Algorithm.HMAC512(signature.toByteArray()))
            .build()
            .verify(token.replace("Bearer ", String.EMPTY))
    }.getOrNull()
}
