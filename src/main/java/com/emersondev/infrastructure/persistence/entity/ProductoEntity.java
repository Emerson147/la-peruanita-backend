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
@Table(name = "productos")
public class ProductoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id")
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "categoria")
  private String categoria;

  @Column(name = "brand")
  private String brand;

  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(name = "cost", precision = 10, scale = 2)
  private BigDecimal cost;

  @Column(name = "stock")
  private Integer stock;

  @Column(name = "min_stock")
  private Integer minStock;

  @Column(name = "sizes", columnDefinition = "TEXT[]")
  private List<String> sizes;

  @Column(name = "colors", columnDefinition = "TEXT[]")
  private List<String> colors;

  @Column(name = "image")
  private String image;

  @Column(name = "barcode")
  private String barcode;

  @Column(name = "status")
  private String status;

  @Version
  @Column(name = "version")
  private Long version = 0L;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}