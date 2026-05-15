package com.emersondev.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAchievement {

  private UUID id;
  private UUID userId;
  private UUID achievementId;
  private String achievementKey;
  private String title;
  private String description;
  private String icon;
  private String category;
  private String tier;
  private Integer requirement;
  private Integer points;
  private Integer progress;
  private boolean unlocked;
  private LocalDateTime unlockedAt;

  // Regla de negocio
  public boolean puedeDesbloquear() {
    return !this.unlocked &&
            this.progress != null &&
            this.requirement != null &&
            this.progress >= this.requirement;
  }

  public void desbloquear() {
    this.unlocked = true;
    this.unlockedAt = LocalDateTime.now();
  }
}