package br.alfredopaes.my_plant_backend.users

import br.alfredopaes.my_plant_backend.excepetions.BadRequestExcepetion
import br.alfredopaes.my_plant_backend.plants.Plant
import br.alfredopaes.my_plant_backend.plants.PlantRepository
import br.alfredopaes.my_plant_backend.plants.requests.PlantRequest
import br.alfredopaes.my_plant_backend.roles.Role
import br.alfredopaes.my_plant_backend.roles.RoleRepository
import br.alfredopaes.my_plant_backend.security.Jwt
import br.alfredopaes.my_plant_backend.users.utils.SortDir
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var plantRepository: PlantRepository

    @Mock
    lateinit var roleRepository: RoleRepository

    @Mock
    lateinit var jwt: Jwt

    @InjectMocks
    lateinit var userService: UserService

    lateinit var user: User
    lateinit var listofUsers: List<User>
    lateinit var plantRequest: PlantRequest

    @BeforeEach
    fun setup() {
        user = User().apply {
            id = 1L
            name = "Test User"
            email = "test@example.com"
            password = "password"
        }
    }

    @BeforeEach
    fun setupListOfUsers() {
        listofUsers = listOf(
            User().apply { id = 1L; name = "Alice"; email = "alice@example.com"; password = "password"; roles = mutableSetOf() },
            User().apply { id = 2L; name = "Bob"; email = "bob@example.com"; password = "password"; roles = mutableSetOf() },
            User().apply { id = 3L; name = "Charlie"; email = "charlie@example.com"; password = "password"; roles = mutableSetOf() }
        )
    }

    @Test
    fun `save deve retornar o usuário salvo`() {
        // Arrange
        `when`(userRepository.save(user)).thenReturn(user)

        // Act
        val savedUser = userService.save(user)

        // Assert
        assertNotNull(savedUser)
        assertEquals(user.id, savedUser.id)
        assertEquals(user.name, savedUser.name)
        assertEquals(user.email, savedUser.email)
        assertEquals(user.password, savedUser.password)

        // Verify that save was called on the repository
        verify(userRepository, times(1)).save(user)
    }

    @Test
    fun `findAll deve retornar os usuários ordenados por email em ordem crescente`() {
        `when`(userRepository.findByEmail("alice@example.com")).thenReturn(listofUsers.filter { it.email == "alice@example.com" })

        val result = userService.findAll(SortDir.ASC, email = "alice@example.com")

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("alice@example.com", result[0].email)

        verify(userRepository, times(1)).findByEmail("alice@example.com")
    }

    @Test
    fun `findAll deve retornar os usuários ordenados por nome em ordem decrescente`() {
        `when`(userRepository.findByName("Bob")).thenReturn(listofUsers.filter { it.name == "Bob" })

        val result = userService.findAll(SortDir.DESC, name = "Bob")

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("Bob", result[0].name)

        verify(userRepository, times(1)).findByName("Bob")
    }

    @Test
    fun `findAll deve retornar os usuários ordenados por função em ordem crescente`() {
        `when`(userRepository.findByRole("ADMIN")).thenReturn(listofUsers.filter { "ADMIN" in it.roles.map { role -> role.name } })

        val result = userService.findAll(SortDir.ASC, role = "ADMIN")

        assertNotNull(result)
        assertTrue(result.isEmpty())

        verify(userRepository, times(1)).findByRole("ADMIN")
    }

    @Test
    fun `findAll deve retornar todos os usuários ordenados por nome em ordem crescente`() {
        `when`(userRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(listofUsers.sortedBy { it.name })

        val result = userService.findAll(SortDir.ASC)

        assertNotNull(result)
        assertEquals(3, result.size)
        assertEquals("Alice", result[0].name)
        assertEquals("Bob", result[1].name)
        assertEquals("Charlie", result[2].name)

        verify(userRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "name"))
    }

    @Test
    fun `findById deve retornar o usuário quando o usuário existir`() {
        val user = User().apply {
            id = 1L
            name = "John Doe"
            email = "john.doe@example.com"
            password = "password"
            roles = mutableSetOf()
        }
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))

        val result = userService.findById(1L)

        assertNotNull(result)
        assertEquals(1L, result?.id)
        assertEquals("John Doe", result?.name)
        assertEquals("john.doe@example.com", result?.email)

        verify(userRepository, times(1)).findById(1L)
    }

    @Test
    fun `findById deve retornar null quando o usuário não existir`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        val result = userService.findById(1L)

        assertNull(result)

        verify(userRepository, times(1)).findById(1L)
    }

    @Test
    fun `delete deve retornar false quando o usuário não for encontrado`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        val result = userService.delete(1L)

        assertFalse(result)
        verify(userRepository, never()).delete(any(User::class.java))
    }

    @Test
    fun `delete deve deletar o usuário e retornar true quando o usuário não for um administrador`() {
        val user = User().apply {
            id = 1L
            roles = mutableSetOf(Role(name = "USER"))
        }
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))

        val result = userService.delete(1L)

        assertTrue(result)
        verify(userRepository, times(1)).delete(user)
    }

    @Test
    fun `delete deve deletar o usuário e retornar true quando o usuário for um administrador, mas não o último`() {
        val user = User().apply {
            id = 1L
            roles = mutableSetOf(Role(name = "ADMIN"))
        }
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(roleRepository.findRoleByName("ADMIN")).thenReturn(Role(name = "ADMIN"))
        `when`(roleRepository.countByName("ADMIN")).thenReturn(2L)

        val result = userService.delete(1L)

        assertTrue(result)
        verify(userRepository, times(1)).delete(user)
    }

    @Test
    fun `delete deve lançar BadRequestException quando o usuário for o último administrador`() {
        val user = User().apply {
            id = 1L
            roles = mutableSetOf(Role(name = "ADMIN"))
        }
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(roleRepository.findRoleByName("ADMIN")).thenReturn(Role(name = "ADMIN"))
        `when`(roleRepository.countByName("ADMIN")).thenReturn(1L)

        val exception = assertThrows(BadRequestExcepetion::class.java) {
            userService.delete(1L)
        }

        assertEquals("Não pode deletar último Administrador", exception.message)
        verify(userRepository, never()).delete(any(User::class.java))
    }

    @Test
    fun `updateUser deve atualizar os detalhes do usuário e retornar o usuário atualizado`() {
        val existingUser = User().apply {
            id = 1L
            name = "Existing Name"
            email = "existing@example.com"
            password = "existingPassword"
        }
        val userDetails = User().apply {
            id = 1L
            name = "Updated Name"
            email = "updated@example.com"
            password = "updatedPassword"
        }

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(existingUser))
        `when`(userRepository.save(any(User::class.java))).thenAnswer { invocation ->
            val userArg = invocation.arguments[0] as User
            existingUser.apply {
                name = userArg.name
                email = userArg.email
                password = userArg.password
            }
        }

        val updatedUser = userService.updateUser(1L, userDetails)

        assertNotNull(updatedUser)
        assertEquals(userDetails.name, updatedUser!!.name)
        assertEquals(userDetails.email, updatedUser.email)
        assertEquals(userDetails.password, updatedUser.password)
        verify(userRepository, times(1)).findById(1L)
        verify(userRepository, times(1)).save(any(User::class.java))
    }

    @Test
    fun `updateUser deve retornar null quando o usuário não for encontrado`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        val updatedUser = userService.updateUser(1L, User())

        assertNull(updatedUser)
        verify(userRepository, times(1)).findById(1L)
        verify(userRepository, never()).save(any(User::class.java))
    }

    @Test
    fun `teste de login com credenciais válidas`() {
        val email = "test@example.com"
        val password = "password"
        val user = User(id = 1L, name = "Test User", email = email, password = password)
        `when`(userRepository.findByEmail(email)).thenReturn(listOf(user))
        `when`(jwt.createToken(user)).thenReturn("mocked_token")

        val loginResponse = userService.login(email, password)

        assertEquals(user.id, loginResponse?.user?.id)
        assertEquals("mocked_token", loginResponse?.token)
    }

    @Test
    fun `teste de login com usuário não encontrado`() {
        val email = "nonexistent@example.com"
        `when`(userRepository.findByEmail(email)).thenReturn(emptyList())

        val loginResponse = userService.login(email, "password")

        assertNull(loginResponse)
    }

    @Test
    fun `teste de login com senha inválida`() {
        val email = "test@example.com"
        val user = User(id = 1L, name = "Test User", email = email, password = "password123")
        `when`(userRepository.findByEmail(email)).thenReturn(listOf(user))

        val loginResponse = userService.login(email, "password")

        assertNull(loginResponse)
    }

    @Test
    fun `teste addPlantToUser com usuário válido e requisição de planta`() {
        val userId = 1L
        val plantRequest = PlantRequest(namePlant = "Tomate", typePlant = "Fruta", "08/06/2026", "15:00")
        val user = User(id = userId, name = "Test User")
        val plant = plantRequest.toPlant().apply {
            this.user = user
        }

        `when`(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user))
        `when`(plantRepository.save(any())).thenReturn(plant)

        val result = userService.addPlantToUser(userId, plantRequest)

        assertEquals(plant.namePlant, result?.namePlant)
        assertEquals(user, result?.user)
        verify(plantRepository).save(any())
    }

    @Test
    fun `teste updatePlant com sucesso`() {
        val user = User(id = 1L, name = "Test User")
        val plant = Plant(id = 1L, namePlant = "Rose", user = user)
        `when`(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user))
        `when`(plantRepository.findById(1L)).thenReturn(java.util.Optional.of(plant))

        val plantRequest = PlantRequest(namePlant = "Tomate Cereja", typePlant = "Fruta", "08/07/2026", "15:00")
        val updatedPlant = Plant(
            id = 1L,
            namePlant = plantRequest.namePlant,
            typePlant = plantRequest.typePlant,
            validityOfPlantingLand = plantRequest.validityOfPlantingLand,
            timeToWaterThePlant = plantRequest.timeToWaterThePlant,
            user = user
        )
        `when`(plantRepository.save(any())).thenReturn(updatedPlant)

        val result = userService.updatePlant(1L, 1L, plantRequest)

        assertEquals(plantRequest.namePlant, result?.namePlant)
        assertEquals(plantRequest.typePlant, result?.typePlant)
        assertEquals(plantRequest.validityOfPlantingLand, result?.validityOfPlantingLand)
        assertEquals(plantRequest.timeToWaterThePlant, result?.timeToWaterThePlant)
        verify(plantRepository).save(any())
    }

    @Test
    fun `teste deletePlant quando palnta não é encontrada`() {
        val user = User(id = 1L, name = "Test User")
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(plantRepository.findById(1L)).thenReturn(Optional.empty())

        val result = userService.deletePlant(1L, 1L)

        assertFalse(result)
        verify(plantRepository, never()).delete(any())
    }

    @Test
    fun `test deletePlant bem sucedido`() {
        val user = User(id = 1L, name = "Test User")
        val plant = Plant(id = 1L, namePlant = "Rose", user = user)
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(plantRepository.findById(1L)).thenReturn(Optional.of(plant))

        val result = userService.deletePlant(1L, 1L)

        assertTrue(result)
        verify(plantRepository).delete(plant)
    }
}