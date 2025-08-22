# RestauranteApp — Tech Challenge (Fase 2)

API para gestão de **Tipos de Usuário**, **Usuários**, **Restaurantes** e **Itens de Cardápio**.  
Desenvolvida com **Spring Boot**, **PostgreSQL** e executada via **Docker Compose**.

---

## Escopo da Fase 2

**Funcionalidades**:  
  - CRUD de **Tipos de Usuário**  
  - CRUD de **Usuários** (vinculados a um Tipo de Usuário)  
  - CRUD de **Restaurantes** (vinculados a um Usuário dono)  
  - CRUD de **Itens de Cardápio** (vinculados a um Restaurante)  

**Arquitetura**

: Clean Architecture  
  - domain/ — modelos, regras de negócio  
  - application.service/ — orquestra casos de uso  
  - infrastructure/ — controllers, persistência, response DTOs
  
**Entrega**:

  - Documentação (`README.md`)  
  - Execução via Docker Compose (Java + PostgreSQL)  
  - Postman Collections para testes  

> Observação: Tem um video, demonstrando aplicação funcional

---

## Baixar aplicação
```bash
git clone https://github.com/Rogerice/techchallenge.restauranteapp
   ```

## Gerar o pacote
```bash
mvn clean package
   ```
## Requisitos

- Docker Desktop (Windows/Mac/Linux)  
- Portas livres: **8080** (app) e **5432** (Postgres)

---

## Executando com Docker Compose

1. **Build + subir containers**

   ```bash
   docker compose up -d --build
   ```

2. **Ver logs**

   ```bash
   docker compose logs -f app
   docker compose logs -f db
   ```

3. **Healthcheck**

   ```bash
   curl http://localhost:8080/actuator/health
   # {"status":"UP"}
   ```

4. **Parar containers**

   ```bash
   docker compose down
   # (opcional) resetar banco:
   docker compose down -v
   ```

---

## Postman — Collections

Na pasta `/postman/` você encontra:

**RestauranteApp API Collection**
- `Restaurantes.postman_collection.json`
- `Cardápio.postman_collection.json`
- `TiposUsuarios.postman_collection.json` 
- `Usuarios.postman_collection.json` 


---

## Endpoints principais

### Tipos de Usuário
- `POST /api/tipos-usuario`
- `GET /api/tipos-usuario`
- `GET /api/tipos-usuario/{id}`
- `PUT /api/tipos-usuario/{id}`
- `DELETE /api/tipos-usuario/{id}`

### Usuários
- `POST /api/usuarios`
- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `PUT /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

### Restaurantes
- `POST /api/restaurantes`
- `GET /api/restaurantes`
- `GET /api/restaurantes/{id}`
- `PUT /api/restaurantes/{id}`
- `DELETE /api/restaurantes/{id}`

### Itens de Cardápio
- `POST /api/cardapio/itens/restaurantes/{restauranteId}`
- `GET /api/cardapio/itens`
- `GET /api/cardapio/itens/{id}`
- `PUT /api/cardapio/itens/{id}`
- `DELETE /api/cardapio/itens/{id}`

---

## Troubleshooting

- **Porta ocupada**: mude os mapeamentos em docker-compose.yml (`8081:8080`, `15432:5432`).  
- **App não conecta ao banco**: rode docker compose logs -f app e docker compose logs -f db.  
- **Windows + arquivos reservados**: evite criar arquivos `NUL`, `CON`, `PRN`, etc., pois quebram o Docker BuildKit.
