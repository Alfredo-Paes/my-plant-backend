package br.alfredopaes.my_plant_backend.roles

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/roles")
@RestController
class RoleController(
    val roleService: RoleService
) {
    @PostMapping
    fun insert(@Valid @RequestBody role: RoleRequest) =
        role.toRole()
            .let { roleService.insert(it) }
            .let { RoleResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping
    fun listAllRoles(): ResponseEntity<List<RoleResponse>> {
        val roles = roleService.listAllRoles().map { RoleResponse(it) }
        return ResponseEntity.ok(roles)
    }


}