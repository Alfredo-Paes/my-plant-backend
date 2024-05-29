package br.alfredopaes.my_plant_backend.users

import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun save(user: User): User {
        return userRepository.save(user);
    }

    fun findAll(): List<User> {
        return userRepository.findAll();
    }

    fun findById(id: Long): User? {
        return userRepository.findById(id);
    }
}