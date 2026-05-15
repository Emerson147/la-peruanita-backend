package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.Achievement;
import com.emersondev.domain.model.UserAchievement;
import com.emersondev.infrastructure.persistence.entity.AchievementEntity;
import com.emersondev.infrastructure.persistence.entity.UserAchievementEntity;
import org.springframework.stereotype.Component;

@Component
public class AchievementMapper {

  public AchievementEntity toEntity(Achievement a) {
    if (a == null) return null;
    AchievementEntity e = new AchievementEntity();
    e.setId(a.getId());
    e.setAchievementKey(a.getAchievementKey());
    e.setTitle(a.getTitle());
    e.setDescription(a.getDescription());
    e.setIcon(a.getIcon());
    e.setCategory(a.getCategory());
    e.setTier(a.getTier());
    e.setRequirement(a.getRequirement());
    e.setPoints(a.getPoints());
    return e;
  }

  public Achievement toDomain(AchievementEntity e) {
    if (e == null) return null;
    Achievement a = new Achievement();
    a.setId(e.getId());
    a.setAchievementKey(e.getAchievementKey());
    a.setTitle(e.getTitle());
    a.setDescription(e.getDescription());
    a.setIcon(e.getIcon());
    a.setCategory(e.getCategory());
    a.setTier(e.getTier());
    a.setRequirement(e.getRequirement());
    a.setPoints(e.getPoints());

    return a;
  }

  public UserAchievement toUserDomain(UserAchievementEntity e) {
    if (e == null) return null;
    UserAchievement ua = new UserAchievement();
    ua.setId(e.getId());
    ua.setUserId(e.getUserId());
    ua.setAchievementId(e.getAchievement().getId());
    ua.setAchievementKey(e.getAchievement().getAchievementKey());
    ua.setTitle(e.getAchievement().getTitle());
    ua.setDescription(e.getAchievement().getDescription());
    ua.setIcon(e.getAchievement().getIcon());
    ua.setCategory(e.getAchievement().getCategory());
    ua.setTier(e.getAchievement().getTier());
    ua.setRequirement(e.getAchievement().getRequirement());
    ua.setPoints(e.getAchievement().getPoints());
    ua.setProgress(e.getProgress());
    ua.setUnlocked(e.isUnlocked());
    ua.setUnlockedAt(e.getUnlockedAt());
    return ua;
  }


}
