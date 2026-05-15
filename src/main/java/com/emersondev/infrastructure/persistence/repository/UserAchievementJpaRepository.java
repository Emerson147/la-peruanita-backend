package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.UserAchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAchievementJpaRepository
        extends JpaRepository<UserAchievementEntity, UUID> {

  List<UserAchievementEntity> findByUserId(UUID userId);

  Optional<UserAchievementEntity> findByUserIdAndAchievement_AchievementKey(
          UUID userId, String key);
}