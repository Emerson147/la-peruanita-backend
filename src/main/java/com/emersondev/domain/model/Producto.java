package com.emersondev.domain.model;

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
public class Producto {

  private UUID id;
  private String name;
  private String categoria;
  private String brand;
  private BigDecimal price;
  private BigDecimal cost;
  private Integer stock;
  private Integer minStock;
  private List<String> sizes;
  private List<String> colors;
  private String image;
  private String barcode;
  private String status;
  private Long version;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<Variante> variantes;

  // Reglas de negocio del dominio
  public boolean tieneStockBajo() {
    return this.stock <= this.minStock;
  }

  public boolean estaActivo() {
    return "active".equals(this.status);
  }

  public BigDecimal calcularGanancia() {
    return this.price.subtract(this.cost);
  }

  public void descontarStock(int cantidad) {
    if (this.stock < cantidad) {
      throw new RuntimeException("Stock insuficiente para: " + this.name);
    }
    this.stock -= cantidad;
  }
}