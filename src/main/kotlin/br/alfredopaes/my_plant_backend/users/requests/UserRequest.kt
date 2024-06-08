package br.alfredopaes.my_plant_backend.users.requests

import br.alfredopaes.my_plant_backend.users.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserRequest(
    @field: Email(message = "Formato de email inválido!")
    val email: String?,
    @field:Size(min = 5, max = 50)
    val password: String?,
    @field:NotBlank(message = "Insira um nome!")
    val name: String?
) {
    fun toUser(): User = User(
        email = email!!,
        password = password!!,
        name = name ?: ""
    )
}
