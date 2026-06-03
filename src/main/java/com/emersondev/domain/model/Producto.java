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
  private List<String> sizes;
  private List<String> colors;
  private String image;
  private String barcode;
  private String codigoInterno;
  private String status;
  private Long version;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<Variante> variantes;

  // Reglas de negocio del dominio
  public Integer getStockTotal() {
    if (this.variantes == null) return 0;
    return this.variantes.stream()
            .mapToInt(Variante::getStockTotal)
            .sum();
  }

  public boolean estaActivo() {
    return "active".equals(this.status);
  }

  public BigDecimal calcularGanancia() {
    return this.price.subtract(this.cost);
  }
}