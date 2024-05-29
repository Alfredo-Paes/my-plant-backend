package br.alfredopaes.my_plant_backend.users

import org.springframework.stereotype.Component

@Component
class UserRepository {
    private var lastId = 0L;
    private val users: MutableMap<Long, User> = mutableMapOf<Long, User>();

    fun save(user: User): User {
        val id: Long? = user.id;
        if (id == null) {
            lastId += 1
            user.id = lastId
            users[lastId] = user
        } else {
            users[id] = user;
        }
        return user;
    };

    fun findAll(): List<User> {
        return users.values.toList();
    }

    fun findById(id: Long): User? {
        return users[id];
    }
}