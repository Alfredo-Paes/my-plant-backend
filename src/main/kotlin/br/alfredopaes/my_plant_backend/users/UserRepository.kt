package br.alfredopaes.my_plant_backend.users


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long>{
    fun findByEmail(email: String): List<User>;
    fun findByName(name: String): List<User>;

    @Query(
        "select distinct u from User u" +
                " join u.roles r" +
                " where r.name = :role" +
                " order by u.name"
    )
    fun findByRole(role: String): List<User>
}