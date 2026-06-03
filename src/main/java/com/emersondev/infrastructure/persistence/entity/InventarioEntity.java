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
@Table(name = "inventarios")
public class InventarioEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "variante_id", nullable = false)
  private UUID varianteId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "almacen_id", nullable = false)
  private AlmacenEntity almacen;

  @Column(name = "stock", nullable = false)
  private Integer stock = 0;

  @Column(name = "min_stock", nullable = false)
  private Integer minStock = 5;

  @Version
  @Column(name = "version")
  private Long version = 0L;
}
