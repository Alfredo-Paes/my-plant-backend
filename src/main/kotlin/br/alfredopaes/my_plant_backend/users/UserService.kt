package br.alfredopaes.my_plant_backend.users

import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun save(user: User): User = userRepository.save(user);

    fun findAll(dir: SortDir): List<User> =
        if (dir == SortDir.ASC) userRepository.findAll().sortedBy { it.name }
        else userRepository.findAll().sortedByDescending { it.name }

    fun findById(id: Long) = userRepository.findById(id);

    fun delete(id: Long) = userRepository.delete(id);
}