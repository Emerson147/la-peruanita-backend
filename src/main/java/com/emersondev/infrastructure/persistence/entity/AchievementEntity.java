package com.emersondev.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "achievements")
public class AchievementEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "achievement_key", unique = true, nullable = false)
  private String achievementKey;

  @Column(nullable = false)
  private String title;

  @Column
  private String description;

  @Column
  private String icon;

  @Column
  private String category;

  @Column
  private String tier;

  @Column
  private Integer requirement;

  @Column
  private Integer points;
}