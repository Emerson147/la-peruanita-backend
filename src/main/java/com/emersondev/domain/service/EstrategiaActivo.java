package com.emersondev.domain.service;

import com.emersondev.domain.model.LiquidacionProducto;
import org.springframework.stereotype.Component;

// Estrategia para productos ACTIVOS (<30 días sin vender)
@Component
public class EstrategiaActivo implements EstrategiaLiquidacion {

  @Override
  public boolean aplica(Integer diasSinVender) {
    return diasSinVender < 30; // <30 días = activo
  }

  @Override
  public void aplicar(LiquidacionProducto liquidacion) {
    liquidacion.setEstado("ACTIVO");
  }

  @Override
  public String nombre() {
    return "ACTIVO - menos de 30 días sin vender";
  }
}