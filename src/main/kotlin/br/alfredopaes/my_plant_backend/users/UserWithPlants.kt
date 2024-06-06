package br.alfredopaes.my_plant_backend.users

import br.alfredopaes.my_plant_backend.plants.PlantRequest

data class UserWithPlants(
    val id: Long,
    val name: String,
    val email: String,
    val plants: List<PlantRequest>
) {
}
