package com.emersondev.domain.repository;

import com.emersondev.domain.model.Achievement;
import com.emersondev.domain.model.UserAchievement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AchievementRepository {
  Achievement save(Achievement achievement);
  List<Achievement> findAll();
  Optional<Achievement> findByKey(String key);

  UserAchievement saveUserAchievement(UserAchievement ua);
  List<UserAchievement> findByUserId(UUID userId);
  Optional<UserAchievement> findByUserIdAndKey(
          UUID userId, String key);
  boolean existsByKey(String key);
}