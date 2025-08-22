package com.techchallenge.restauranteapp.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import com.techchallenge.restauranteapp.domain.enums.TipoUsuarioEnum;
import com.techchallenge.restauranteapp.domain.model.entity.RestauranteEntity;
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
class RestauranteJpaRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private RestauranteJpaRepository repo;

    private UsuarioEntity novoUsuario(String nome, String email) {
        UsuarioEntity u = new UsuarioEntity();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenha("123");
        u.setTipoUsuario(TipoUsuarioEnum.DONO);
        return u;
    }

    private RestauranteEntity novoRestaurante(String nome) {
        UsuarioEntity dono = em.persistAndFlush(novoUsuario("Dono " + nome, nome.toLowerCase() + "@ex.com"));

        RestauranteEntity r = new RestauranteEntity();
        r.setNome(nome);
        r.setCnpj("12345678000" + ((int) (Math.random() * 9)));
        r.setEndereco("Rua X, 123");
        r.setHorarioFuncionamento("08:00-18:00");
        r.setTipoCozinha("BRASILEIRA");
        r.setDono(dono);
        return r;
    }

    @Test
    @DisplayName("save() deve persistir restaurante e permitir consulta por id")
    void salvarEBuscar() {
        RestauranteEntity r1 = repo.saveAndFlush(novoRestaurante("Casa da Esfiha"));

        assertNotNull(r1.getId());

        var opt = repo.findById(r1.getId());
        assertTrue(opt.isPresent());

        RestauranteEntity found = opt.get();
        assertEquals("Casa da Esfiha", found.getNome());
        assertEquals("BRASILEIRA", found.getTipoCozinha());
        assertNotNull(found.getDono());
    }


    @Test
    @DisplayName("delete() deve remover restaurante existente")
    void deletar() {
        RestauranteEntity r1 = repo.saveAndFlush(novoRestaurante("RDelete"));

        Long id = r1.getId();
        repo.deleteById(id);
        repo.flush();

        assertFalse(repo.findById(id).isPresent());
    }
}
