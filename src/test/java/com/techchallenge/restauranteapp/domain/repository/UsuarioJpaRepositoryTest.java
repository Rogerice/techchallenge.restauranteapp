package com.techchallenge.restauranteapp.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.dao.DataIntegrityViolationException;

import com.techchallenge.restauranteapp.domain.enums.TipoUsuarioEnum;
import com.techchallenge.restauranteapp.domain.model.entity.UsuarioEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:restaurante;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false",
        "spring.liquibase.enabled=false",
        "spring.jpa.show-sql=false",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class UsuarioJpaRepositoryTest {

    @Autowired
    private UsuarioJpaRepository repo;

    private UsuarioEntity novoUsuario(String nome, String email) {
        UsuarioEntity u = new UsuarioEntity();
        u.setNome(nome);
        u.setEmail(email);          // unique not null
        u.setSenha("123");          // not null
        u.setTipoUsuario(TipoUsuarioEnum.CLIENTE); // enum check constraint
        return u;
    }

    @Test
    @DisplayName("save/findById: deve persistir e recuperar usuário")
    void salvarEBuscar() {
        var salvo = repo.saveAndFlush(novoUsuario("Ana", "ana@ex.com"));
        assertNotNull(salvo.getId());

        var opt = repo.findById(salvo.getId());
        assertTrue(opt.isPresent());
        assertEquals("Ana", opt.get().getNome());
        assertEquals(TipoUsuarioEnum.CLIENTE, opt.get().getTipoUsuario());
    }

    @Test
    @DisplayName("findAll: deve listar usuários persistidos")
    void listarTodos() {
        repo.saveAndFlush(novoUsuario("Bob", "bob@ex.com"));
        repo.saveAndFlush(novoUsuario("Carol", "carol@ex.com"));
        var todos = repo.findAll();
        assertTrue(todos.size() >= 2);
    }

    @Test
    @DisplayName("update: deve atualizar dados mantendo o id")
    void atualizar() {
        var u = repo.saveAndFlush(novoUsuario("Dan", "dan@ex.com"));
        Long id = u.getId();

        u.setNome("Danilo");
        u.setTipoUsuario(TipoUsuarioEnum.DONO);
        var atualizado = repo.saveAndFlush(u);

        assertEquals(id, atualizado.getId());
        assertEquals("Danilo", atualizado.getNome());
        assertEquals(TipoUsuarioEnum.DONO, atualizado.getTipoUsuario());
    }

    @Test
    @DisplayName("deleteById: deve remover usuário existente")
    void deletar() {
        var u = repo.saveAndFlush(novoUsuario("Eva", "eva@ex.com"));
        Long id = u.getId();

        repo.deleteById(id);
        repo.flush();

        assertTrue(repo.findById(id).isEmpty());
    }

    @Test
    @DisplayName("unique(email): deve lançar DataIntegrityViolation ao salvar email duplicado")
    void emailDuplicado() {
        repo.saveAndFlush(novoUsuario("Foo", "dup@ex.com"));
        assertThrows(DataIntegrityViolationException.class, () ->
                repo.saveAndFlush(novoUsuario("Bar", "dup@ex.com")));
    }
}
