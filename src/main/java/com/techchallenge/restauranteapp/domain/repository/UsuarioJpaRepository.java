package com.techchallenge.restauranteapp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techchallenge.restauranteapp.domain.model.entity.UsuarioEntity;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {
}
