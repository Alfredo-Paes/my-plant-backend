package br.alfredopaes.my_plant_backend.users

import br.alfredopaes.my_plant_backend.plants.Plant
import br.alfredopaes.my_plant_backend.plants.PlantRepository
import br.alfredopaes.my_plant_backend.plants.PlantRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val plantRepository: PlantRepository
) {
    fun save(user: User): User = userRepository.save(user);

    fun findAll(dir: SortDir, email: String? = null, name: String? = null): List<User> {
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

    }



    fun findById(id: Long): User? = userRepository.findById(id).orElse(null);

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