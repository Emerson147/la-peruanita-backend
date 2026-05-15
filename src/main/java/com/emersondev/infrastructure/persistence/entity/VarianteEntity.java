package com.emersondev.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "variantes")
public class VarianteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "product_id", nullable = false)
  private UUID productId;

  @Column(name = "size", nullable = false)
  private String size;

  @Column(name = "color")
  private String color;

  @Column(name = "stock")
  private Integer stock;

  @Column(name = "barcode")
  private String barcode;

  @Version
  @Column(name = "version")
  private Long version = 0L;
}
