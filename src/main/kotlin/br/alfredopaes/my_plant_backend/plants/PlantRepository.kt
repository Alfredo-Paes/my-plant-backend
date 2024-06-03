package br.alfredopaes.my_plant_backend.plants

import org.springframework.data.jpa.repository.JpaRepository

interface PlantRepository: JpaRepository<Plant, Long> {
    fun findByNamePlant(namePlant: String): List<Plant>
}