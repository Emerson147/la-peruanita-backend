package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AchievementJpaRepository
        extends JpaRepository<AchievementEntity, UUID> {

  Optional<AchievementEntity> findByAchievementKey(String key);
  boolean existsByAchievementKey(String key);
}

