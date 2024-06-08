package br.alfredopaes.my_plant_backend.users

import br.alfredopaes.my_plant_backend.plants.Plant
import br.alfredopaes.my_plant_backend.plants.PlantRepository
import br.alfredopaes.my_plant_backend.plants.PlantRequest
import br.alfredopaes.my_plant_backend.roles.RoleRepository
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val plantRepository: PlantRepository,
    val roleRepository: RoleRepository,

    ) {
    fun save(user: User): User = userRepository.save(user);

    /*fun findAll(dir: SortDir, role: String?, email: String? = null, name: String? = null): List<User> {
        return if (email.isNullOrEmpty()) {
            when(dir) {
                SortDir.ASC->userRepository.findAll(Sort.by("name").ascending())
                SortDir.DESC->userRepository.findAll(Sort.by("name").descending())
            }
        } else if (name.isNullOrEmpty()){
            userRepository.findByEmail(email).sortedByDescending { it.email };
        } else {
            userRepository.findByName(name).sortedByDescending { it.name };
        }

    }*/

    fun findAll(dir: SortDir, role: String? = null, email: String? = null, name: String? = null): List<User> {
        return when {
            !email.isNullOrEmpty() -> userRepository.findByEmail(email).sortedWith(
                compareBy(if (dir == SortDir.ASC) Comparator.naturalOrder<String>() else Comparator.reverseOrder<String>()) { it.email }
            )
            !name.isNullOrEmpty() -> userRepository.findByName(name).sortedWith(
                compareBy(if (dir == SortDir.ASC) Comparator.naturalOrder<String>() else Comparator.reverseOrder<String>()) { it.name }
            )
            !role.isNullOrEmpty() -> userRepository.findByRole(role.uppercase()).sortedWith(
                compareBy(if (dir == SortDir.ASC) Comparator.naturalOrder<String>() else Comparator.reverseOrder<String>()) { it.name }
            )
            else -> userRepository.findAll(Sort.by(if (dir == SortDir.ASC) Sort.Direction.ASC else Sort.Direction.DESC, "name"))
        }
    }



    fun findById(id: Long): User? = userRepository.findByIdOrNull(id);

    fun delete(id: Long): User? {
        val user = userRepository.findById(id).orElse(null)
        user?.let {
            userRepository.deleteById(id);
        }
        return user;
    }

    fun updateUser(id: Long, userDetails: User): User? {
        val existingUser = userRepository.findById(id).orElse(null) ?: return null
        existingUser.name = userDetails.name
        existingUser.email = userDetails.email
        existingUser.password = userDetails.password
        return userRepository.save(existingUser)
    }

    fun addRole(id: Long, roleName: String): Boolean {
        val user = userRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("User $id not found!")

        if (user.roles.any { it.name == roleName }) return false

        val role = roleRepository.findRoleByName(roleName)
            ?: throw IllegalArgumentException("Invalid role $roleName!")

        user.roles.add(role)
        userRepository.save(user)
        return true
    }

    fun addPlantToUser(userId: Long, plantRequest: PlantRequest): Plant? {
        val user = userRepository.findById(userId).orElse(null) ?: return null
        val plant = plantRequest.toPlant().apply {
            this.user = user
        }
        return plantRepository.save(plant)
    }

    fun updatePlant(userId: Long, plantId: Long, plantDetails: PlantRequest): Plant? {
        val user = userRepository.findById(userId).orElse(null) ?: return null
        val existingPlant = plantRepository.findById(plantId).orElse(null) ?: return null
        if (existingPlant.user?.id != user.id) return null

        existingPlant.namePlant = plantDetails.namePlant;
        existingPlant.typePlant = plantDetails.typePlant;
        existingPlant.validityOfPlantingLand = plantDetails.validityOfPlantingLand;
        existingPlant.timeToWaterThePlant = plantDetails.timeToWaterThePlant;

        return plantRepository.save(existingPlant);
    }

    fun deletePlant(userId: Long, plantId: Long): Boolean {
        val user = userRepository.findById(userId).orElse(null) ?: return false
        val plant = plantRepository.findById(plantId).orElse(null) ?: return false
        if (plant.user?.id != user.id) return false
        plantRepository.delete(plant)
        return true
    }
}