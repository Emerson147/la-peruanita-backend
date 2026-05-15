package com.emersondev.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventario {

  private UUID id;
  private String movementNumber;
  private String type; // entrada | ajuste
  private UUID productId;
  private UUID varianteId;
  private String productName;
  private Integer quantity;
  private Integer quantityBefore;
  private Integer quantityAfter;
  private String reason;
  private String supplier;
  private String invoice;
  private BigDecimal unitCost;
  private BigDecimal totalCost;
  private String createdBy;
  private String notes;
  private LocalDateTime createdAt;

  // Regla de negocio — calcular total
  public void calcularTotalCost() {
    if (this.unitCost != null && this.quantity != null) {
      this.totalCost = this.unitCost.multiply(
              BigDecimal.valueOf(this.quantity));
    }
  }

  // Regla de negocio — es una entrada de stock
  public boolean esEntrada() {
    return "entrada".equals(this.type);
  }

  // Regla de negocio — es un ajuste manual
  public boolean esAjuste() {
    return "ajuste".equals(this.type);
  }

}