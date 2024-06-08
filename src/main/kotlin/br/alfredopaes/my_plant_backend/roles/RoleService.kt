package br.alfredopaes.my_plant_backend.roles

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class RoleService(val roleRepository: RoleRepository) {
    fun insert(role: Role): Role = roleRepository.save(role)
    fun listAllRoles(): List<Role> =  roleRepository.findAll(Sort.by("name").ascending())
}