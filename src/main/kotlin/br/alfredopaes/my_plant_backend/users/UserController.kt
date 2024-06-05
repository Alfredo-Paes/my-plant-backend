package br.alfredopaes.my_plant_backend.users

import br.alfredopaes.my_plant_backend.plants.Plant
import br.alfredopaes.my_plant_backend.plants.PlantRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService
) {

    @PostMapping()
    fun insert(@RequestBody @Validated user: UserRequest): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user.toUser()));
    }

    @GetMapping
    fun findAll(
        @RequestParam sortDir: String? = null,
        @RequestParam email: String? = null,
        @RequestParam name: String? = null
    ): ResponseEntity<List<User>> {
        val direction = SortDir.entries.firstOrNull { it.name == (sortDir ?: "ASC").uppercase() }
        return direction?.let { ResponseEntity.ok(userService.findAll(it, email, name)) }
            ?: ResponseEntity.badRequest().build()
    }

    @GetMapping("/{id}")
    fun findbyId(
        @PathVariable(value = "id") id: Long
    ) = userService.findById(id)
        ?.let { ResponseEntity.ok(it) }
        ?:ResponseEntity.status(404).body("Usuário não encontrado!")


    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable id: Long
    ): ResponseEntity<String> =
        userService.delete(id)
            ?.let { ResponseEntity.ok("Usuário ${it.name} removido com sucesso!") }
            ?:ResponseEntity.status(404).body("Usuário não encontrado!")

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Validated user: UserRequest
    ): ResponseEntity<Any> {
        val updatedUser = userService.updateUser(id, user.toUser())
        return updatedUser
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(404).body("Usuário não encontrado!")
    }

    /*@PostMapping("/{id}/plants")
    fun addPlantToUser(
        @PathVariable(value = "id") userId: Long,
        @RequestBody @Validated plantRequest: PlantRequest
    ): ResponseEntity<Any> {
        if (plantRequest.userId != userId) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID mismatch!")
        }
        val addedPlant = userService.addPlantToUser(plantRequest)
        return addedPlant
            ?.let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
            ?: ResponseEntity.status(404).body("Usuário não encontrado!")
    }*/

    @PostMapping("/{id}/plants")
    fun addPlantToUser(
        @PathVariable(value = "id") userId: Long,
        @RequestBody @Validated plantRequest: PlantRequest
    ): ResponseEntity<Any> {
        val addedPlant = userService.addPlantToUser(userId, plantRequest)
        return addedPlant
            ?.let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
            ?: ResponseEntity.status(404).body("Usuário não encontrado!")
    }
}