package br.alfredopaes.my_plant_backend.users

import br.alfredopaes.my_plant_backend.plants.Plant
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
@Table(name="tbUser")
class User (
    @Id @GeneratedValue
    var id: Long? = null,
    @Column(unique = true, nullable = false)
    var email: String = "",
    @NotNull
    var password: String = "",
    @NotNull
    var name: String = "",

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    var plants: List<Plant> = mutableListOf()
){
}