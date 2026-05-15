package com.emersondev.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {

  private UUID id;
  private String achievementKey; // ach-first-sale, ach-10-sales, etc
  private String title;
  private String description;
  private String icon;
  private String category; // sales, revenue, streak, special
  private String tier;     // bronze, silver, gold, platinum
  private Integer requirement;
  private Integer points;

}