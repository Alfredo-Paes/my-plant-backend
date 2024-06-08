package br.alfredopaes.my_plant_backend.security

import br.alfredopaes.my_plant_backend.users.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserToken(
    val id: Long,
    val name: String,
    val roles: Set<String>
) {
    constructor(): this(0, "", setOf())
    constructor(user: User): this(
        id = user.id!!,
        name = user.name,
        roles = user.roles.map { it.name }.toSortedSet()
    )

    @get:JsonIgnore
    val isAdmin: Boolean get() = "ADMIN" in roles

    fun toAuthentication(): UsernamePasswordAuthenticationToken {
        val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }
        return UsernamePasswordAuthenticationToken(name, null, authorities)
    }
}
