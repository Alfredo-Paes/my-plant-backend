package br.alfredopaes.my_plant_backend.security

import br.alfredopaes.my_plant_backend.users.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Component
class Jwt {
    fun createToken(user: User): String =
        UserToken(user).let {
            Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                .serializeToJsonWith(JacksonSerializer())
                .setIssuedAt(utcNow().toDate())
                .setExpiration(utcNow().plusHours(
                    if (it.isAdmin) ADMIN_EXPIRE_HOURS else EXPIRE_HOURS).toDate()
                )
                .setIssuer(ISSUER)
                .setSubject(user.id.toString())
                .claim(USER_FIELD, it)
                .compact()
        }

    fun extract(req: HttpServletRequest): Authentication? {
        try {
            val header = req.getHeader(AUTHORIZATION)
            if (header == null || !header.startsWith("Bearer ")) return null
            val token = header.replace("Bearer ", "").trim()

            val claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                .deserializeJsonWith(JacksonDeserializer(mapOf(USER_FIELD to UserToken::class.java)))
                .build()
                .parseClaimsJws(token)
                .body

            if (claims.issuer != ISSUER) return null
            return claims.get(USER_FIELD, UserToken::class.java)?.toAuthentication()
        } catch (e: Throwable) {
            log.debug("Token rejected", e)
            return null
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(Jwt::class.java)

        val SECRET = "3083ad22d244d5c07b025575402beefb743df30b"
        const val EXPIRE_HOURS = 48L
        const val ISSUER = "MyPlantBackEnd"
        const val USER_FIELD = "user"
        const val ADMIN_EXPIRE_HOURS = 1L


        private fun utcNow() = ZonedDateTime.now(ZoneOffset.UTC)
        private fun ZonedDateTime.toDate(): Date = Date.from(this.toInstant())
        private fun UserToken.toAuthentication(): Authentication {
            val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }
            return UsernamePasswordAuthenticationToken.authenticated(this, id, authorities)
        }
    }
}