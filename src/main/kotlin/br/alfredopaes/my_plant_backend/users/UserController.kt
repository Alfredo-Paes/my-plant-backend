package br.alfredopaes.my_plant_backend.users

import br.alfredopaes.my_plant_backend.plants.requests.PlantRequest
import br.alfredopaes.my_plant_backend.users.requests.LoginRequest
import br.alfredopaes.my_plant_backend.users.requests.UserRequest
import br.alfredopaes.my_plant_backend.users.responses.UserResponse
import br.alfredopaes.my_plant_backend.users.utils.SortDir
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
    ) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody login: LoginRequest) =
        userService.login(login.email!!, login.password!!)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

    @PostMapping()
    fun insert(@RequestBody @Validated user: UserRequest): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user.toUser()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name="WebToken")
    fun findAll(
        @RequestParam sortDir: String? = null,
        @RequestParam role: String? = null,
        @RequestParam email: String? = null,
        @RequestParam name: String? = null
    ): ResponseEntity<List<UserResponse>> =
        SortDir.entries.firstOrNull { it.name == (sortDir ?: "ASC").uppercase() }
            ?.let { userService.findAll(it, role, email, name) }
            ?.map { UserResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.badRequest().build()


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name="WebToken")
    fun findbyId(
        @PathVariable(value = "id") id: Long
    ) = userService.findById(id)
        ?.let { UserResponse(it) }
        ?.let { ResponseEntity.ok(it) }
        ?:ResponseEntity.status(404).body("Usuário não encontrado!")

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name="WebToken")
    fun deleteById(
        @PathVariable id: Long
    ): ResponseEntity<String> =
        userService.delete(id)
            ?.let { UserResponse(it) }
            ?.let { ResponseEntity.ok("Usuário ${it.name} removido com sucesso!") }
            ?:ResponseEntity.status(404).body("Usuário não encontrado!")

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Validated user: UserRequest
    ): ResponseEntity<Any> {
        val updatedUser = userService.updateUser(id, user.toUser())
        return updatedUser
            ?.let { UserResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(404).body("Usuário não encontrado!")
    }

    @PutMapping("/{id}/roles/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name="WebToken")
    fun grant(
        @PathVariable id: Long,
        @PathVariable role: String
    ): ResponseEntity<Void> =
        if (userService.addRole(id, role)) ResponseEntity.ok().build()
        else ResponseEntity.noContent().build()

    /**
     * Métodos referentes a edição e remoção de uma planta que estão vinculados a um usuário
     */
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

    @PutMapping("/{userId}/plants/{plantId}")
    fun updatePlant(
        @PathVariable userId: Long,
        @PathVariable plantId: Long,
        @RequestBody @Validated plantRequest: PlantRequest
    ): ResponseEntity<Any> {
        val updatedPlant = userService.updatePlant(userId, plantId, plantRequest)
        return updatedPlant
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(404).body("Usuário ou planta não encontrado!")
    }

    @DeleteMapping("/{userId}/plants/{plantId}")
    fun deletePlant(
        @PathVariable userId: Long,
        @PathVariable plantId: Long
    ): ResponseEntity<String> {
        val success = userService.deletePlant(userId, plantId)
        return if (success) {
            ResponseEntity.ok("Planta removida com sucesso!")
        } else {
            ResponseEntity.status(404).body("Usuário ou planta não encontrado!")
        }
    }
}