package com.techchallenge.restauranteapp.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.techchallenge.restauranteapp.domain.model.entity.TipoUsuarioEntity;

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
class TipoUsuarioJpaRepositoryTest {

    @Autowired
    private TipoUsuarioJpaRepository repo;

    private TipoUsuarioEntity novoTipo(String nomeTipo) {
        TipoUsuarioEntity t = new TipoUsuarioEntity();
        t.setNomeTipo(nomeTipo); // campo not null
        return t;
    }

    @Test
    @DisplayName("save/findById: deve persistir e recuperar tipo de usuário")
    void salvarEBuscar() {
        var salvo = repo.saveAndFlush(novoTipo("GERENTE"));
        assertNotNull(salvo.getId());

        var opt = repo.findById(salvo.getId());
        assertTrue(opt.isPresent());
        assertEquals("GERENTE", opt.get().getNomeTipo());
    }

    @Test
    @DisplayName("findAll: deve listar tipos persistidos")
    void listarTodos() {
        repo.saveAndFlush(novoTipo("CAIXA"));
        repo.saveAndFlush(novoTipo("GARCOM"));
        var todos = repo.findAll();
        assertTrue(todos.size() >= 2);
    }

    @Test
    @DisplayName("deleteById: deve remover tipo existente")
    void deletar() {
        var t = repo.saveAndFlush(novoTipo("TEMP"));
        Long id = t.getId();

        repo.deleteById(id);
        repo.flush();

        assertTrue(repo.findById(id).isEmpty());
    }
}
