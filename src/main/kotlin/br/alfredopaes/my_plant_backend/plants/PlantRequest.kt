package br.alfredopaes.my_plant_backend.plants

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
