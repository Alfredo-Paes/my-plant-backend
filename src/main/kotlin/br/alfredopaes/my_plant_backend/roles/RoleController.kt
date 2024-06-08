package br.alfredopaes.my_plant_backend.roles

import br.alfredopaes.my_plant_backend.roles.requests.RoleRequest
import br.alfredopaes.my_plant_backend.roles.responses.RoleResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name="WebToken")
    fun listAllRoles(): ResponseEntity<List<RoleResponse>> {
        val roles = roleService.listAllRoles().map { RoleResponse(it) }
        return ResponseEntity.ok(roles)
    }


}