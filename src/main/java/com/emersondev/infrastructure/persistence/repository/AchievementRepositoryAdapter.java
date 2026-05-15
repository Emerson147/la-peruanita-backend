package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.domain.model.Achievement;
import com.emersondev.domain.model.UserAchievement;
import com.emersondev.domain.repository.AchievementRepository;
import com.emersondev.infrastructure.persistence.entity.AchievementEntity;
import com.emersondev.infrastructure.persistence.entity.UserAchievementEntity;
import com.emersondev.infrastructure.persistence.mapper.AchievementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AchievementRepositoryAdapter
        implements AchievementRepository {

  private final AchievementJpaRepository achievementJpa;
  private final UserAchievementJpaRepository userAchievementJpa;
  private final AchievementMapper mapper;

  @Override
  public Achievement save(Achievement achievement) {
    return mapper.toDomain(
            achievementJpa.save(mapper.toEntity(achievement)));
  }

  @Override
  public List<Achievement> findAll() {
    return achievementJpa.findAll().stream()
            .map(mapper::toDomain).toList();
  }

  @Override
  public Optional<Achievement> findByKey(String key) {
    return achievementJpa.findByAchievementKey(key)
            .map(mapper::toDomain);
  }

  @Override
  public boolean existsByKey(String key) {
    return achievementJpa.existsByAchievementKey(key);
  }

  @Override
  public UserAchievement saveUserAchievement(
          UserAchievement ua) {
    // Buscar la entity del achievement
    AchievementEntity achEntity = achievementJpa
            .findByAchievementKey(ua.getAchievementKey())
            .orElseThrow(() -> new RuntimeException(
                    "Achievement no encontrado: " +
                            ua.getAchievementKey()));

    // Buscar si ya existe
    UserAchievementEntity entity =
            userAchievementJpa.findByUserIdAndAchievement_AchievementKey(
                            ua.getUserId(), ua.getAchievementKey())
                    .orElseGet(UserAchievementEntity::new);

    entity.setUserId(ua.getUserId());
    entity.setAchievement(achEntity);
    entity.setProgress(ua.getProgress());
    entity.setUnlocked(ua.isUnlocked());
    entity.setUnlockedAt(ua.getUnlockedAt());

    return mapper.toUserDomain(
            userAchievementJpa.save(entity));
  }

  @Override
  public List<UserAchievement> findByUserId(UUID userId) {
    return userAchievementJpa.findByUserId(userId)
            .stream()
            .map(mapper::toUserDomain)
            .toList();
  }

  @Override
  public Optional<UserAchievement> findByUserIdAndKey(
          UUID userId, String key) {
    return userAchievementJpa
            .findByUserIdAndAchievement_AchievementKey(
                    userId, key)
            .map(mapper::toUserDomain);
  }
}