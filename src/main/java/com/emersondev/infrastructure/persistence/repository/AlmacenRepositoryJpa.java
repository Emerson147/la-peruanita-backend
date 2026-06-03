package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.AlmacenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlmacenRepositoryJpa extends JpaRepository<AlmacenEntity, UUID> {
  Optional<AlmacenEntity> findByNombre(String nombre);
}
