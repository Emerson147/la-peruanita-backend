package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.UserStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserStatsJpaRepository extends JpaRepository<UserStatsEntity, UUID> {
  Optional<UserStatsEntity> findByUserId(UUID userId);

}
