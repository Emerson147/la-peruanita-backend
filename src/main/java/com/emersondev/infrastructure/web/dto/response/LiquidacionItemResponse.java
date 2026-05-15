package com.emersondev.infrastructure.web.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public class LiquidacionItemResponse {

  private UUID productoId;
  private String nombre;
  private String categoria;
  private Integer stock;
  private BigDecimal costo;
  private BigDecimal precioActual;
  private Integer diasSinVender;
  private Integer feriasSinVender;
  private Double rotacionPorFeria;
  private String estado;
  private BigDecimal precioConDescuento20;
  private BigDecimal precioConDescuento30;
  private BigDecimal precioConDescuento40;
  private BigDecimal precioCongelado;
  private BigDecimal capitalCongelado;
  private BigDecimal potencialRecuperacion;

  public LiquidacionItemResponse() {}

  public UUID getProductoId() { return productoId; }
  public void setProductoId(UUID productoId) { this.productoId = productoId; }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public String getCategoria() { return categoria; }
  public void setCategoria(String categoria) { this.categoria = categoria; }

  public Integer getStock() { return stock; }
  public void setStock(Integer stock) { this.stock = stock; }

  public BigDecimal getCosto() { return costo; }
  public void setCosto(BigDecimal costo) { this.costo = costo; }

  public BigDecimal getPrecioActual() { return precioActual; }
  public void setPrecioActual(BigDecimal precioActual) { this.precioActual = precioActual; }

  public Integer getDiasSinVender() { return diasSinVender; }
  public void setDiasSinVender(Integer diasSinVender) { this.diasSinVender = diasSinVender; }

  public Integer getFeriasSinVender() { return feriasSinVender; }
  public void setFeriasSinVender(Integer feriasSinVender) { this.feriasSinVender = feriasSinVender; }

  public Double getRotacionPorFeria() { return rotacionPorFeria; }
  public void setRotacionPorFeria(Double rotacionPorFeria) { this.rotacionPorFeria = rotacionPorFeria; }

  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }

  public BigDecimal getPrecioConDescuento20() { return precioConDescuento20; }
  public void setPrecioConDescuento20(BigDecimal p) { this.precioConDescuento20 = p; }

  public BigDecimal getPrecioConDescuento30() { return precioConDescuento30; }
  public void setPrecioConDescuento30(BigDecimal p) { this.precioConDescuento30 = p; }

  public BigDecimal getPrecioConDescuento40() { return precioConDescuento40; }
  public void setPrecioConDescuento40(BigDecimal p) { this.precioConDescuento40 = p; }

  public BigDecimal getPrecioCongelado() { return precioCongelado; }
  public void setPrecioCongelado(BigDecimal p) { this.precioCongelado = p; }

  public BigDecimal getCapitalCongelado() { return capitalCongelado; }
  public void setCapitalCongelado(BigDecimal c) { this.capitalCongelado = c; }

  public BigDecimal getPotencialRecuperacion() { return potencialRecuperacion; }
  public void setPotencialRecuperacion(BigDecimal p) { this.potencialRecuperacion = p; }
}
