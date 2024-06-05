package br.alfredopaes.my_plant_backend.plants

import br.alfredopaes.my_plant_backend.users.User
import br.alfredopaes.my_plant_backend.users.UserRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class PlantService(
    val userRepository: UserRepository,
    val plantRespository: PlantRepository
) {
    fun savePlant(plant: Plant): Plant = plantRespository.save(plant);

    fun findAllPlants(dir: SortDirPlants, namePlant: String? = null): List<Plant> {
        return if (namePlant.isNullOrEmpty()) {
            when(dir) {
                SortDirPlants.ASC->plantRespository.findAll(Sort.by("namePlant").ascending())
                SortDirPlants.DESC->plantRespository.findAll(Sort.by("namePlant").descending())
            }
        } else {
            plantRespository.findByNamePlant(namePlant);
        }

    }

    fun findPlantById(id: Long): Plant? = plantRespository.findById(id).orElse(null);

    fun deletePlant(userId: Long, plantId: Long): Boolean {
        val user = userRepository.findById(userId).orElse(null) ?: return false
        val plant = plantRespository.findById(plantId).orElse(null) ?: return false
        if (plant.user?.id != user.id) return false
        plantRespository.delete(plant)
        return true
    }

    fun updatePlant(userId: Long, plantId: Long, plantDetails: PlantRequest): Plant? {
        val user = userRepository.findById(userId).orElse(null) ?: return null
        val existingPlant = plantRespository.findById(plantId).orElse(null) ?: return null
        if (existingPlant.user?.id != user.id) return null

        existingPlant.namePlant = plantDetails.namePlant;
        existingPlant.typePlant = plantDetails.typePlant;
        existingPlant.validityOfPlantingLand = plantDetails.validityOfPlantingLand;
        existingPlant.timeToWaterThePlant = plantDetails.timeToWaterThePlant;

        return plantRespository.save(existingPlant);
    }
}