package com.emersondev.domain.service;

import com.emersondev.domain.model.LiquidacionProducto;
import org.springframework.stereotype.Component;

// Estrategia para productos LENTOS (30-60 días sin vender)
@Component
public class EstrategiaLento implements EstrategiaLiquidacion {

  @Override
  public boolean aplica(Integer feriasSinVender) {
    return feriasSinVender >= 4 && feriasSinVender <= 8;
  }

  @Override
  public void aplicar(LiquidacionProducto liquidacion) {
    liquidacion.setEstado("LENTO");
  }

  @Override
  public String nombre() {
    return "LENTO - entre 30 y 60 días sin vender";
  }
}