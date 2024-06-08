package br.alfredopaes.my_plant_backend.roles.responses

import br.alfredopaes.my_plant_backend.roles.Role

class RoleResponse(
    val name: String,
    val description: String
) {
    constructor(role: Role): this(
        name = role.name,
        description = role.description
    )
}