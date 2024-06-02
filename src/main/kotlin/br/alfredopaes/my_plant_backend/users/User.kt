package br.alfredopaes.my_plant_backend.users

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
){
}