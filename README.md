# Minha Planta (My Plant) - Projeto de Registro de Plantas
Desenvolvido por: Alfredo Paes da Luz.

## Descrição do Projeto

O Minha Planta é um aplicativo de registro de plantas para usuários. Neste aplicativo, o usuário pode registrar suas plantas, especificando o nome da planta, tipo, validade da terra em que a planta está plantada e o horário para regar. O projeto foi desenvolvido com Spring Boot e Kotlin, utilizando autenticação JWT e banco de dados JPA.
Este projeto pertence ao trabalho final da disciplina de Desenvolvimento de Backend, da Pós-Graduação de Desenvolvimento de Aplicativos Móveis da PUCPR.

## Tecnologias Utilizadas

- **Spring Boot**: Framework para simplificar o desenvolvimento de aplicações Java.
- **Kotlin**: Linguagem de programação utilizada para escrever o código.
- **JWT (JSON Web Token)**: Utilizado para autenticação e autorização.
- **JPA (Java Persistence API)**: Utilizado para interação com o banco de dados.

## My Plant - Rotas da API

### Autenticação e Usuários

- **POST /users/login**: Realiza o login de um usuário.
- **POST /users**: Cria um novo usuário.
- **GET /users**: Retorna uma lista de usuários.
- **GET /users/{id}**: Retorna um usuário pelo ID (Requer o papel `ADMIN`).
- **DELETE /users/{id}**: Deleta um usuário pelo ID (Requer o papel `ADMIN`).
- **PUT /users/{id}**: Atualiza um usuário pelo ID.
- **PUT /users/{id}/roles/{role}**: Adiciona um papel a um usuário (Requer o papel `ADMIN`).

### Plantas de Usuários

- **POST /users/{id}/plants**: Adiciona uma planta a um usuário.
- **PUT /users/{userId}/plants/{plantId}**: Atualiza uma planta de um usuário.
- **DELETE /users/{userId}/plants/{plantId}**: Deleta uma planta de um usuário.

### Papéis

- **POST /roles**: Cria um novo papel.
- **GET /roles**: Retorna uma lista de papéis (Requer o papel `ADMIN`).

### Plantas

- **POST /plants**: Cria uma nova planta.
- **GET /plants**: Retorna uma lista de plantas.
- **GET /plants/{id}**: Retorna uma planta pelo ID.

### Todas as rotas são acessadas através do : http://localhost:8080/api/swagger-ui/index.html?continue#/user-controller/login

### Vídeo de demonstração: 

