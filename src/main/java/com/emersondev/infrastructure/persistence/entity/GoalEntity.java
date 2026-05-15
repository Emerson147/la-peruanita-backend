package com.emersondev.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "goals")
public class GoalEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(nullable = false)
  private String title;

  @Column
  private String description;

  @Column
  private String type;

  @Column
  private String metric;

  @Column(nullable = false)
  private Integer target;

  @Column
  private Integer current;

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  @Column
  private String status;

  @Column
  private String reward;
}