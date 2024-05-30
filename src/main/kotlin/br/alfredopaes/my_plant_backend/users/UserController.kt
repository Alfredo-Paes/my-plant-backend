package br.alfredopaes.my_plant_backend.users

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService
) {

    @PostMapping()
    fun insert(@RequestBody @Validated user: UserRequest): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user.toUser()));
    }

    @GetMapping()
    fun findAll(sortDir: String? = null) =
        SortDir.entries.firstOrNull { it.name == (sortDir?: "ASC").uppercase() }
            ?.let { userService.findAll(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.badRequest().build()

    @GetMapping("/{id}")
    fun findbyId(@PathVariable(value = "id") id: Long) = userService.findById(id)
        ?.let { ResponseEntity.ok(it) }
        ?:ResponseEntity.status(404).body("Usuário não encontrado!")


    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<String> =
        userService.delete(id)
            ?.let { ResponseEntity.ok("Usuário ${it.name} removido com sucesso!") }
            ?:ResponseEntity.status(404).body("Usuário não encontrado!")
}