package br.alfredopaes.my_plant_backend

import br.alfredopaes.my_plant_backend.roles.Role
import br.alfredopaes.my_plant_backend.roles.RoleRepository
import br.alfredopaes.my_plant_backend.users.User
import br.alfredopaes.my_plant_backend.users.UserRepository
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class Bootstrapper(
    val userRepository: UserRepository,
    val rolesRepository: RoleRepository
): ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val adminRole = rolesRepository.findRoleByName("ADMIN")
            ?: rolesRepository
                .save(Role(name = "ADMIN", description = "System administrator"))
                .also { rolesRepository.save(Role(name = "USER", description = "Premium User")) }

        if (userRepository.findByRole(adminRole.name).isEmpty()) {
            val admin = User(
                name = "MyPlant Server Administrator",
                email = "admin@myplant.com",
                password = "admin"
            )
            admin.roles.add(adminRole)
            userRepository.save(admin)
        }
    }

}