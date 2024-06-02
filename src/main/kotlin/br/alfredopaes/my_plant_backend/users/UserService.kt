package br.alfredopaes.my_plant_backend.users

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun save(user: User): User = userRepository.save(user);

    fun findAll(dir: SortDir, email: String? = null, name: String? = null): List<User> {
        return if (email.isNullOrEmpty()) {
            when(dir) {
                SortDir.ASC->userRepository.findAll(Sort.by("name").ascending())
                SortDir.DESC->userRepository.findAll(Sort.by("name").descending())
            }
        } else if (name.isNullOrEmpty()){
            userRepository.findByEmail(email);
        } else {
            userRepository.findByName(name);
        }

    }

    fun findById(id: Long): User? = userRepository.findById(id).orElse(null);

    fun delete(id: Long): User? {
        val user = userRepository.findById(id).orElse(null)
        user?.let {
            userRepository.deleteById(id);
        }
        return user;
    }

    fun updateUser(id: Long, userDetails: User): User? {
        val existingUser = userRepository.findById(id).orElse(null) ?: return null
        existingUser.name = userDetails.name
        existingUser.email = userDetails.email
        existingUser.password = userDetails.password
        return userRepository.save(existingUser)
    }
}