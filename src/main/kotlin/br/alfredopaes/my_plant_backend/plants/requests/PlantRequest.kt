package br.alfredopaes.my_plant_backend.plants.requests

import br.alfredopaes.my_plant_backend.plants.Plant
import jakarta.validation.constraints.NotBlank

data class PlantRequest(
    @NotBlank
    val namePlant: String,
    @NotBlank
    val typePlant: String,
    @NotBlank
    val validityOfPlantingLand: String,
    @NotBlank
    val timeToWaterThePlant: String
) {
    fun toPlant(): Plant = Plant(
        namePlant = namePlant,
        typePlant = typePlant,
        validityOfPlantingLand = validityOfPlantingLand,
        timeToWaterThePlant = timeToWaterThePlant
    )
}
