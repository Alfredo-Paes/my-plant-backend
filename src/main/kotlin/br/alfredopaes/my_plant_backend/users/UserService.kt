package br.alfredopaes.my_plant_backend.users

import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun save(user: User): User = userRepository.save(user);

    fun findAll(): List<User> = userRepository.findAll();

    fun findById(id: Long) = userRepository.findById(id);

    fun delete(id: Long) = userRepository.delete(id);
}