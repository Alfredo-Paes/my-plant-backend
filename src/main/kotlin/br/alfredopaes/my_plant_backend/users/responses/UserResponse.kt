package br.alfredopaes.my_plant_backend.users.responses

import br.alfredopaes.my_plant_backend.users.User

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String
) {
    constructor(user: User): this(
        id = user.id!!,
        name = user.name,
        email = user.email
    )
}
