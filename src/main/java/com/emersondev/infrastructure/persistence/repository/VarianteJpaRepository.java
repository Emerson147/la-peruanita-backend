package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.VarianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VarianteJpaRepository extends JpaRepository<VarianteEntity, UUID> {

  List<VarianteEntity> findByProductId(UUID productId);

  void deleteByProductId(UUID productId);
}
