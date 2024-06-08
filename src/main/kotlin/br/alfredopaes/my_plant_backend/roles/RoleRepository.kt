package br.alfredopaes.my_plant_backend.roles

import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role, Long> {
    fun findRoleByName(nameRole: String): Role?
}
