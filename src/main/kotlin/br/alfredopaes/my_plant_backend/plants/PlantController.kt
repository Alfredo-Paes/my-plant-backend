package br.alfredopaes.my_plant_backend.plants

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/plants")
class PlantController(
    val plantService: PlantService,
) {
    @PostMapping()
    fun insertPlant(@RequestBody @Validated plant: PlantRequest): ResponseEntity<Plant> {
        return ResponseEntity.status(HttpStatus.CREATED).body(plantService.savePlant(plant.toPlant()));
    }

    @GetMapping
    fun findAllPlants(
        @RequestParam sortDirPlant: String? = null,
        @RequestParam namePlant: String? = null
    ): ResponseEntity<List<Plant>> {
        val direction = SortDirPlants.entries.firstOrNull { it.name == (sortDirPlant ?: "ASC").uppercase() }
        return direction?.let { ResponseEntity.ok(plantService.findAllPlants(it, namePlant)) }
            ?: ResponseEntity.badRequest().build()
    }


    @GetMapping("/{id}")
    fun findPlantById(@PathVariable(value = "id") id: Long): ResponseEntity<Any> =
        plantService.findPlantById(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(404).body("Planta não encontrada");
}