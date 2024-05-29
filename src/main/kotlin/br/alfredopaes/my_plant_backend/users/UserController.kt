package br.alfredopaes.my_plant_backend.users

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
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
    fun findAll(): List<User> {
        return userService.findAll()
    }

    @GetMapping("/{id}")
    fun findbyId(@PathVariable(value = "id") id: Long): ResponseEntity<User> {
        val user: User? = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);

    }
}