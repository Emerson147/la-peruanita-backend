package com.emersondev.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Getter
@Builder(toBuilder = true)
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

  public Integer getMinStockTotal() {
    if (this.variantes == null) return 0;
    return this.variantes.stream()
            .mapToInt(Variante::getMinStockTotal)
            .sum();
  }

  public boolean estaActivo() {
    return "active".equals(this.status);
  }

  public List<String> getSizes() {
    if (this.sizes != null && !this.sizes.isEmpty()) return this.sizes;
    if (this.variantes == null || this.variantes.isEmpty()) return null;
    return this.variantes.stream()
            .map(Variante::getSize)
            .filter(java.util.Objects::nonNull)
            .distinct()
            .toList();
  }

  public List<String> getColors() {
    if (this.colors != null && !this.colors.isEmpty()) return this.colors;
    if (this.variantes == null || this.variantes.isEmpty()) return null;
    return this.variantes.stream()
            .map(Variante::getColor)
            .filter(java.util.Objects::nonNull)
            .distinct()
            .toList();
  }

  public BigDecimal calcularGanancia() {
    return this.price.subtract(this.cost);
  }

  public void asignarValoresPorDefectoCreacion() {
    if (this.status == null) {
      this.status = "active";
    }
    if (this.cost == null) {
      this.cost = BigDecimal.ZERO;
    }
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  public void actualizarDatos(Producto datosActualizados) {
    this.name = datosActualizados.getName();
    this.categoria = datosActualizados.getCategoria();
    this.brand = datosActualizados.getBrand();
    this.price = datosActualizados.getPrice();
    this.cost = datosActualizados.getCost();
    this.sizes = datosActualizados.getSizes();
    this.colors = datosActualizados.getColors();
    this.image = datosActualizados.getImage();
    this.barcode = datosActualizados.getBarcode();
    this.status = datosActualizados.getStatus();
    this.updatedAt = LocalDateTime.now();
  }

  public void asignarVariantes(List<Variante> variantes) {
    this.variantes = variantes;
  }
}