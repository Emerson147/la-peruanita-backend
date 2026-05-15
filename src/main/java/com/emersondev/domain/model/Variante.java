package com.emersondev.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Variante {

  private UUID id;
  private UUID productId;
  private String size;
  private String color;
  private Integer stock;
  private String barcode;
  private Long version;

  // Regla de negocio — tiene stock disponible
  public boolean tieneStock() {
    return this.stock != null && this.stock > 0;
  }

  // Regla de negocio — descontar stock
  public void descontarStock(int cantidad) {
    if (cantidad > this.stock) {
      throw new RuntimeException(
              "Stock insuficiente para variante " +
                      this.size + " - " + this.color);
    }
    this.stock -= cantidad;
  }

}
