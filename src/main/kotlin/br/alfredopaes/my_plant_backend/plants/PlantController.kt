package br.alfredopaes.my_plant_backend.plants

import br.alfredopaes.my_plant_backend.users.UserRequest
import br.alfredopaes.my_plant_backend.users.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/plants")
class PlantController(
    val plantService: PlantService,
    val userService: UserService
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

    // Edição e Remoção planta vinculado ao usuário
    @DeleteMapping("/{userId}/plants/{plantId}")
    fun deletePlant(
        @PathVariable userId: Long,
        @PathVariable plantId: Long
    ): ResponseEntity<String> {
        val success = plantService.deletePlant(userId, plantId)
        return if (success) {
            ResponseEntity.ok("Planta removida com sucesso!")
        } else {
            ResponseEntity.status(404).body("Usuário ou planta não encontrado!")
        }
    }

    @PutMapping("/{userId}/plants/{plantId}")
    fun updatePlant(
        @PathVariable userId: Long,
        @PathVariable plantId: Long,
        @RequestBody @Validated plantRequest: PlantRequest
    ): ResponseEntity<Any> {
        val updatedPlant = plantService.updatePlant(userId, plantId, plantRequest)
        return updatedPlant
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(404).body("Usuário ou planta não encontrado!")
    }
}