package com.emersondev.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gastos")
public class GastoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column
  private String category;

  @Column
  private String receipt;

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}