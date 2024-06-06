package br.alfredopaes.my_plant_backend.users


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long>{
    fun findByEmail(email: String): List<User>;
    fun findByName(name: String): List<User>;

    @Query(
        """
        SELECT u.id AS userId, u.name AS userName, u.email, 
               STRING_AGG(p.namePlant, ', ') AS plants
        FROM tbUser u
        LEFT JOIN tbPlant p ON u.id = p.user_id
        GROUP BY u.id, u.name, u.email
        """,
        nativeQuery = true
    )
    fun findUsersWithPlants(): List<Map<String, Any>>
}