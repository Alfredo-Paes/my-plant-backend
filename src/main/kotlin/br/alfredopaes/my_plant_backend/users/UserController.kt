package br.alfredopaes.my_plant_backend.users

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
        @RequestParam email: String? = null
    ): ResponseEntity<List<User>> {
        val direction = SortDir.entries.firstOrNull { it.name == (sortDir ?: "ASC").uppercase() }
        return direction?.let { ResponseEntity.ok(userService.findAll(it, email)) }
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
}