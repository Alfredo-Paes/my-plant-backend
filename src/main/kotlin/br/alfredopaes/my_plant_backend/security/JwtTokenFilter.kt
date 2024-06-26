package br.alfredopaes.my_plant_backend.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

@Component
class JwtTokenFilter(private val jwt: Jwt) : GenericFilterBean() {
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val auth = jwt.extract(req as HttpServletRequest)
        if (auth != null) {
            SecurityContextHolder.getContext().authentication = auth
            println("Autenticação configurada: ${auth.name}")
        } else {
            println("Token JWT inválido ou ausente.")
        }
        chain.doFilter(req, res)
    }
}