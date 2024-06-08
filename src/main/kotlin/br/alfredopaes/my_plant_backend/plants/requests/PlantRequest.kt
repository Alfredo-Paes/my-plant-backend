package br.alfredopaes.my_plant_backend.plants.requests

import br.alfredopaes.my_plant_backend.plants.Plant
import jakarta.validation.constraints.NotBlank

data class PlantRequest(
    @field:NotBlank(message = "Insira um nome para sua planta!")
    val namePlant: String,
    @field:NotBlank(message = "Insira o tipo de sua planta!")
    val typePlant: String,
    @field:NotBlank(message = "Insira a válidade da terra que a sua planta está alocada.")
    val validityOfPlantingLand: String,
    @field:NotBlank(message = "Insira o horário para regar a sua planta.")
    val timeToWaterThePlant: String
    ) {
    fun toPlant(): Plant = Plant(
        namePlant = namePlant,
        typePlant = typePlant,
        validityOfPlantingLand = validityOfPlantingLand,
        timeToWaterThePlant = timeToWaterThePlant
    )
}
