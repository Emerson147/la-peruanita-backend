package com.emersondev.domain.model;

import com.emersondev.domain.exception.StockInsuficienteException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventario {

  private UUID id;
  private UUID varianteId;
  private UUID almacenId;
  private String nombreAlmacen;
  private Integer stock;
  private Integer minStock;

  // Regla de negocio
  public void descontarStock(int cantidad) {
    if (this.stock < cantidad) {
      throw new StockInsuficienteException("Stock insuficiente en el almacén: " + this.almacenId);
    }
    this.stock -= cantidad;
  }

  public boolean tieneStockBajo() {
    return this.stock <= this.minStock;
  }
}
