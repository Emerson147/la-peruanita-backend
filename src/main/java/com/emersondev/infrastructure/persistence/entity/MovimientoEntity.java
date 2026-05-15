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
@Table(name = "movimientos_inventario")
public class MovimientoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "movement_number", unique = true)
  private String movementNumber;

  @Column(nullable = false)
  private String type;

  @Column(name = "product_id", nullable = false)
  private UUID productId;

  @Column(name = "variante_id")
  private UUID varianteId;

  @Column(name = "product_name")
  private String productName;

  @Column(nullable = false)
  private Integer quantity;

  @Column(name = "quantity_before")
  private Integer quantityBefore;

  @Column(name = "quantity_after")
  private Integer quantityAfter;

  @Column(nullable = false)
  private String reason;

  @Column
  private String supplier;

  @Column
  private String invoice;

  @Column(name = "unit_cost")
  private BigDecimal unitCost;

  @Column(name = "total_cost")
  private BigDecimal totalCost;

  @Column(name = "created_by")
  private String createdBy;

  @Column
  private String notes;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}