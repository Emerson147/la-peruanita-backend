package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.ConfiguracionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConfiguracionJpaRepository extends JpaRepository<ConfiguracionEntity, UUID> {
}
