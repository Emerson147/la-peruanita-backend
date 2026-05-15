package com.emersondev.domain.service;

import com.emersondev.domain.model.LiquidacionProducto;

// Esta es la INTERFAZ del Strategy Pattern
// Define el contrato que todas las estrategias deben cumplir
public interface EstrategiaLiquidacion {

  // ¿Esta estrategia aplica para este producto?
  boolean aplica(Integer diasSinVender);

  // Aplicar la estrategia al producto
  void aplicar(LiquidacionProducto liquidacion);

  // Nombre de la estrategia para logs
  String nombre();

}