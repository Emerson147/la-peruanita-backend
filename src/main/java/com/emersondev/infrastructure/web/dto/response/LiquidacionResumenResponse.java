package com.emersondev.infrastructure.web.dto.response;

import java.math.BigDecimal;

public class LiquidacionResumenResponse {

  private BigDecimal totalCapital;
  private BigDecimal capitalActivo;
  private BigDecimal capitalLento;
  private BigDecimal capitalCongelado;
  private BigDecimal metaLiberacion;
  private BigDecimal ratioLiquidez;
  private Long productosCongelados;
  private Long productosLentos;
  private Long productosActivos;

  public LiquidacionResumenResponse() {}

  public BigDecimal getTotalCapital() { return totalCapital; }
  public void setTotalCapital(BigDecimal t) { this.totalCapital = t; }

  public BigDecimal getCapitalActivo() { return capitalActivo; }
  public void setCapitalActivo(BigDecimal c) { this.capitalActivo = c; }

  public BigDecimal getCapitalLento() { return capitalLento; }
  public void setCapitalLento(BigDecimal c) { this.capitalLento = c; }

  public BigDecimal getCapitalCongelado() { return capitalCongelado; }
  public void setCapitalCongelado(BigDecimal c) { this.capitalCongelado = c; }

  public BigDecimal getMetaLiberacion() { return metaLiberacion; }
  public void setMetaLiberacion(BigDecimal m) { this.metaLiberacion = m; }

  public BigDecimal getRatioLiquidez() { return ratioLiquidez; }
  public void setRatioLiquidez(BigDecimal r) { this.ratioLiquidez = r; }

  public Long getProductosCongelados() { return productosCongelados; }
  public void setProductosCongelados(Long p) { this.productosCongelados = p; }

  public Long getProductosLentos() { return productosLentos; }
  public void setProductosLentos(Long p) { this.productosLentos = p; }

  public Long getProductosActivos() { return productosActivos; }
  public void setProductosActivos(Long p) { this.productosActivos = p; }
}
