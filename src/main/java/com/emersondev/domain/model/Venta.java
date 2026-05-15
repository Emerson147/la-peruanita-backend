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
public class Venta {

  private UUID id;
  private String saleNumber;
  private UUID userId;
  private UUID vendedorId;
  private UUID customerId;
  private List<VentaItem> items;
  private BigDecimal subtotal;
  private BigDecimal discount;
  private BigDecimal tax;
  private BigDecimal total;
  private String paymentMethod;
  private String status;
  private String notes;
  private String createdBy;
  private LocalDateTime createdAt;

  // Regla de negocio 1 — calcular subtotal desde los items
  public void calcularSubtotal() {
    if (items == null || items.isEmpty()) {
      this.subtotal = BigDecimal.ZERO;
      return;
    }
    this.subtotal = items.stream()
            .map(VentaItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  // Regla de negocio 2 — calcular total con IGV y descuento
  public void calcularTotal() {
    BigDecimal descuento = this.discount != null
            ? this.discount : BigDecimal.ZERO;

    // Los precios de los items YA incluyen IGV. 
    // Por lo tanto, el total a pagar es simplemente la suma de los items menos el descuento.
    this.total = this.subtotal.subtract(descuento);

    // Si queremos ser estrictos con la contabilidad en la BD:
    // El "subtotal" real (base imponible) debería ser el total / 1.18
    // Y el "tax" (IGV) sería total - base imponible.
    // Como el front asume que 'subtotal' es la suma de los items, dejaremos que el front maneje la presentación,
    // o podemos sobrescribir el tax para asegurar que matemáticamente cuadre.
    BigDecimal divisorIgv = new BigDecimal("1.18");
    BigDecimal baseImponible = this.total.divide(divisorIgv, 2, java.math.RoundingMode.HALF_UP);
    this.tax = this.total.subtract(baseImponible);
  }

  // Regla de negocio 3 — verificar si está completada
  public boolean estaCompletada() {
    return "completed".equals(this.status);
  }

  // Regla de negocio 4 — verificar si se puede cancelar
  public boolean sePuedeCancelar() {
    return "pending".equals(this.status)
            || "completed".equals(this.status);
  }

}
