package br.alfredopaes.my_plant_backend.plants

import br.alfredopaes.my_plant_backend.users.UserRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class PlantService(
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
}