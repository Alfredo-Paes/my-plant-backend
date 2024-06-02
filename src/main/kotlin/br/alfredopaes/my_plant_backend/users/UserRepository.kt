package br.alfredopaes.my_plant_backend.users


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long>{
    fun findByEmail(email: String): List<User>;
    fun findByName(name: String): List<User>;
}