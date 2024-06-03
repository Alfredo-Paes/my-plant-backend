package br.alfredopaes.my_plant_backend.plants

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "tbPlant")
class Plant(
    @Id @GeneratedValue
    var id: Long? = null,
    @Column(unique = false, nullable = false)
    var namePlant: String = "",
    @NotNull
    var typePlant: String = "",
    @NotNull
    var validityOfPlantingLand: String = "",
    @NotNull
    var timeToWaterThePlant: String = "",

)