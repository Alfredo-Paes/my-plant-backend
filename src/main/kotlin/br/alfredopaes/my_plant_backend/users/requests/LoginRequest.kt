package br.alfredopaes.my_plant_backend.users.requests

data class LoginRequest(
    val email: String?,
    val password: String?
)
