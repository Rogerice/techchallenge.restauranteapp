package com.techchallenge.restauranteapp.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techchallenge.restauranteapp.domain.model.entity.RestauranteEntity;

@Repository
public interface RestauranteJpaRepository extends JpaRepository<RestauranteEntity, Long> {
}
