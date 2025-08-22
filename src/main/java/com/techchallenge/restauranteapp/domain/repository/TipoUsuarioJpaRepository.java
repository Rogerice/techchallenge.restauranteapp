package com.techchallenge.restauranteapp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techchallenge.restauranteapp.domain.model.entity.TipoUsuarioEntity;

public interface TipoUsuarioJpaRepository extends JpaRepository<TipoUsuarioEntity, Long> {
}
