package br.alfredopaes.my_plant_backend.roles

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Role(
    @Id @GeneratedValue
    var id: Long? = null,

    @Column(unique = true, nullable = false)
    var name: String,

    var description: String = "",
) {
    // Construtor padrão necessário para o Hibernate
    constructor() : this(name = "", description = "")
}