package br.alfredopaes.my_plant_backend.roles

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Sort

@ExtendWith(MockitoExtension::class)
class RoleServiceTest {

    @Mock
    lateinit var roleRepository: RoleRepository

    @InjectMocks
    lateinit var roleService: RoleService

    @Test
    fun `teste inserir papel`() {
        // Criando uma instância de Role manualmente sem usar copy()
        val role = Role(1L,"ROLE_USER", "Regular User Role")

        // Configurando o comportamento do mock do repository
        val savedRole = Role(1L,"ROLE_USER", "Regular User Role")
        savedRole.id = 1L // Simulando o ID retornado após o save()

        // Configurando o comportamento do mock para o método save
        doReturn(savedRole).`when`(roleRepository).save(any())

        // Executando o método a ser testado
        val result = roleService.insert(role)

        // Verificando o resultado
        assertEquals(savedRole, result)
    }

    @Test
    fun `istAllRoles deve retornar papéis (ou funções) ordenados por nome`() {
        val role1 = Role(1L, "ADMIN", "Administrator role")
        val role2 = Role(2L, "USER", "User role")
        val roles = listOf(role1, role2)

        `when`(roleRepository.findAll(Sort.by("name").ascending())).thenReturn(roles)

        val sortedRoles = roleService.listAllRoles()

        assertEquals(2, sortedRoles.size)
        assertEquals("ADMIN", sortedRoles[0].name)
        assertEquals("USER", sortedRoles[1].name)
    }
}