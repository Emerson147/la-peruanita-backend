package com.emersondev.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class ClienteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column
  private String phone;

  @Column
  private String email;

  @Column
  private String address;

  @Column(name = "total_purchases")
  private BigDecimal totalPurchases;

  @Column(name = "last_purchase_date")
  private LocalDateTime lastPurchaseDate;

  @Column(nullable = false)
  private String tier = "nuevo";

  @Column(columnDefinition = "TEXT[]")
  private List<String> preferences;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}