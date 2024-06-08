package br.alfredopaes.my_plant_backend.plants.responses

import br.alfredopaes.my_plant_backend.plants.Plant

class PlantResponse(
    val namePlant: String,
    val typePlant: String,
    val validityOfPlantingLand: String,
    val timeToWaterThePlant: String
) {
    constructor(plant: Plant): this(
        namePlant = plant.namePlant,
        typePlant = plant.typePlant,
        validityOfPlantingLand = plant.validityOfPlantingLand,
        timeToWaterThePlant = plant.timeToWaterThePlant
    )
}