package com.emersondev.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
public class LiquidacionProducto {

  private Producto producto;
  private Integer diasSinVender;
  private Integer feriasSinVender;     // ← nuevo campo clave
  private Double rotacionPorFeria;     // ← nuevo campo clave
  private BigDecimal precioConDescuento20;
  private BigDecimal precioConDescuento30;
  private BigDecimal precioConDescuento40;
  private BigDecimal precioCongelado;
  private String estado; // ACTIVO, LENTO, CONGELADO
  private BigDecimal capitalCongelado;
  private BigDecimal potencialRecuperacion; // ← nuevo campo

  // Regla de Negocio — calcular precios y capital congelado
  public void calcularPrecios() {
    BigDecimal precio = producto.getPrice();
    BigDecimal costo = producto.getCost();
    Integer stock = producto.getStock();

    this.precioConDescuento20 = calcularPrecioConDescuento(precio, 0.20);
    this.precioConDescuento30 = calcularPrecioConDescuento(precio, 0.30);
    this.precioConDescuento40 = calcularPrecioConDescuento(precio, 0.40);

    // Precio congelado — mínimo para no perder dinero (+10% sobre costo)
    this.precioCongelado = costo
            .multiply(BigDecimal.valueOf(1.10))
            .setScale(2, RoundingMode.HALF_UP);

    // Capital atrapado en este producto
    this.capitalCongelado = costo
            .multiply(BigDecimal.valueOf(stock));

    // Potencial de recuperación vendiendo al -20%
    this.potencialRecuperacion = precioConDescuento20
            .subtract(costo)
            .multiply(BigDecimal.valueOf(stock));
  }

  private BigDecimal calcularPrecioConDescuento(
          BigDecimal precio, double porcentaje) {
    return precio
            .subtract(precio.multiply(BigDecimal.valueOf(porcentaje)))
            .setScale(2, RoundingMode.HALF_UP);
  }

}