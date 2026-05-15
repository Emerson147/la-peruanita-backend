package com.emersondev.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaItem {

  private UUID id;
  private UUID saleId;
  private UUID productId;
  private UUID varianteId;
  private String productName;
  private Integer quantity;
  private String size;
  private String color;
  private BigDecimal unitPrice;
  private BigDecimal subtotal;

  // Regla de negocio — calcular subtotal
  public void calcularSubtotal() {
    this.subtotal = this.unitPrice
            .multiply(BigDecimal.valueOf(this.quantity));
  }

}
