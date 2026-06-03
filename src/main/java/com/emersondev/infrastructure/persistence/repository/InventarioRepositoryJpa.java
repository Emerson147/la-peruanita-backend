package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventarioRepositoryJpa extends JpaRepository<InventarioEntity, UUID> {
  Optional<InventarioEntity> findByVarianteIdAndAlmacenId(UUID varianteId, UUID almacenId);
  List<InventarioEntity> findByVarianteId(UUID varianteId);
  List<InventarioEntity> findByAlmacenId(UUID almacenId);
}
