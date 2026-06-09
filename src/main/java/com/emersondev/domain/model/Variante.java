package com.emersondev.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Variante {

  private UUID id;
  private UUID productId;
  private UUID almacenId;
  private String size;
  private String color;
  private String barcode;
  private Long version;
  private String almacenName;
  private List<Inventario> inventarios;



  public Integer getStockTotal() {
    if (this.inventarios == null) return 0;
    return this.inventarios.stream()
            .mapToInt(Inventario::getStock)
            .sum();
  }

  public Integer getMinStockTotal() {
    if (this.inventarios == null) return 0;
    return this.inventarios.stream()
            .mapToInt(inv -> inv.getMinStock() != null ? inv.getMinStock() : 0)
            .sum();
  }

  // Regla de negocio — tiene stock disponible
  public boolean tieneStock() {
    return getStockTotal() > 0;
  }

}
