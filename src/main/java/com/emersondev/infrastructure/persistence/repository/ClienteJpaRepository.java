package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, UUID> {

  Optional<ClienteEntity> findByPhone(String phone);
  List<ClienteEntity> findByTier(String tier);

  Page<ClienteEntity> findAll(Pageable pageable);
  Page<ClienteEntity> findByTier(String tier, Pageable pageable);
}
