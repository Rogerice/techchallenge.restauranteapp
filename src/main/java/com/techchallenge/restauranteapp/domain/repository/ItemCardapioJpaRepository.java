package com.techchallenge.restauranteapp.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techchallenge.restauranteapp.domain.model.entity.ItemCardapioEntity;

public interface ItemCardapioJpaRepository extends JpaRepository<ItemCardapioEntity, Long> {

    @EntityGraph(attributePaths = {"restaurante"})
    @Query("select i from ItemCardapioEntity i where i.id = :id")
    Optional<ItemCardapioEntity> findOneById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"restaurante"})
    List<ItemCardapioEntity> findAllByRestaurante_Id(Long restauranteId);

    @EntityGraph(attributePaths = {"restaurante"})
    List<ItemCardapioEntity> findAll();

    boolean existsByRestaurante_IdAndNomeIgnoreCase(Long restauranteId, String nome);
}
