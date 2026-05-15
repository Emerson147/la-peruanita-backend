package com.emersondev.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_achievements", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "achievement_id"}))
public class UserAchievementEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "achievement_id", nullable = false)
  private AchievementEntity achievement;

  @Column
  private Integer progress;

  @Column
  private boolean unlocked;

  @Column(name = "unlocked_at")
  private LocalDateTime unlockedAt;
}