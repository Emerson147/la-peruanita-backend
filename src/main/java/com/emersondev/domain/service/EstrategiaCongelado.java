package com.emersondev.domain.service;

import com.emersondev.domain.model.LiquidacionProducto;
import org.springframework.stereotype.Component;

// Estrategia para productos CONGELADOS (>60 días sin vender)
@Component
public class EstrategiaCongelado implements EstrategiaLiquidacion {

  @Override
  public boolean aplica(Integer diasSinVender) {
    return diasSinVender > 60; // >60 días = congelado
  }

  @Override
  public void aplicar(LiquidacionProducto liquidacion) {
    liquidacion.setEstado("CONGELADO");
    // Para productos congelados recomendamos precio mínimo
    // El precio congelado ya fue calculado en calcularPrecios()
  }

  @Override
  public String nombre() {
    return "CONGELADO - más de 60 días sin vender";
  }
}