package com.emersondev.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_stats")
public class UserStatsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "user_id", unique = true, nullable = false)
  private UUID userId;

  @Column(name = "total_points")
  private Integer totalPoints;

  @Column
  private Integer level;

  @Column(name = "current_streak")
  private Integer currentStreak;

  @Column(name = "longest_streak")
  private Integer longestStreak;

  @Column(name = "total_sales_completed")
  private Integer totalSalesCompleted;

  @Column(name = "total_revenue_generated")
  private BigDecimal totalRevenueGenerated;

  @Column(name = "joined_at")
  private LocalDateTime joinedAt;
}