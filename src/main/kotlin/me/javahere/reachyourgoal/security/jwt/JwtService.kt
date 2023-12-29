package me.javahere.reachyourgoal.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import me.javahere.reachyourgoal.util.EMPTY
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service
class JwtService(
    @Value("\${app.secret}") val secret: String,
    @Value("\${app.refresh}") val refresh: String
) {

    companion object {
        val EXPIRE_ACCESS_TOKEN = Duration.ofMinutes(15).toMillis()
        val EXPIRE_REFRESH_TOKEN = Duration.ofHours(4).toMillis()
        val EXPIRE_CONFIRMATION_TOKEN = Duration.ofMinutes(5).toMillis()
    }

    fun accessToken(username: String, expirationInMillis: Long, roles: Array<String>): String =
        generate(username, expirationInMillis, roles, secret)

    fun decodeAccessToken(accessToken: String): DecodedJWT = decode(secret, accessToken)

    fun refreshToken(username: String, expirationInMillis: Long, roles: Array<String>): String =
        generate(username, expirationInMillis, roles, refresh)

    fun decodeRefreshToken(refreshToken: String): DecodedJWT = decode(refresh, refreshToken)

    fun getRoles(decodedJWT: DecodedJWT) = decodedJWT.getClaim("role").asList(String::class.java)
        .map { SimpleGrantedAuthority(it) }

    fun getRoles(token: String): List<SimpleGrantedAuthority> = getRoles(decodeAccessToken(token))
    fun getUsername(token: String): String = decodeAccessToken(token).subject

    fun generate(username: String, expirationInMillis: Long, roles: Array<String>, signature: String): String =
        JWT.create()
            .withSubject(username)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationInMillis))
            .withArrayClaim("role", roles)
            .sign(Algorithm.HMAC512(signature.toByteArray()))

    private fun decode(signature: String, token: String): DecodedJWT =
        JWT.require(Algorithm.HMAC512(signature.toByteArray()))
            .build()
            .verify(token.replace("Bearer ", String.EMPTY))
}
