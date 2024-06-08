package br.alfredopaes.my_plant_backend.users.responses

data class LoginResponse(
    val token: String,
    val user: UserResponse
)
