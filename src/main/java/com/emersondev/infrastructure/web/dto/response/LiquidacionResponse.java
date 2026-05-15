package com.emersondev.infrastructure.web.dto.response;

import java.util.List;

public class LiquidacionResponse {

  private LiquidacionResumenResponse resumen;
  private List<LiquidacionItemResponse> productos;

  public LiquidacionResponse() {}

  public LiquidacionResponse(
          LiquidacionResumenResponse resumen,
          List<LiquidacionItemResponse> productos) {
    this.resumen = resumen;
    this.productos = productos;
  }

  public LiquidacionResumenResponse getResumen() { return resumen; }
  public void setResumen(LiquidacionResumenResponse resumen) {
    this.resumen = resumen;
  }

  public List<LiquidacionItemResponse> getProductos() { return productos; }
  public void setProductos(List<LiquidacionItemResponse> productos) {
    this.productos = productos;
  }
}
