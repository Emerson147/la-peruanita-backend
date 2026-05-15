package com.emersondev.infrastructure.web.controller;

import com.emersondev.domain.model.LiquidacionProducto;
import com.emersondev.domain.service.LiquidacionService;
import com.emersondev.infrastructure.web.dto.response.LiquidacionItemResponse;
import com.emersondev.infrastructure.web.dto.response.LiquidacionResponse;
import com.emersondev.infrastructure.web.dto.response.LiquidacionResumenResponse;
import com.emersondev.infrastructure.web.mapper.LiquidacionDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/liquidacion")
@RequiredArgsConstructor
public class LiquidacionController {

  private final LiquidacionService liquidacionService;
  private final LiquidacionDtoMapper mapper;

  // GET /api/liquidacion
  // Devuelve resumen + todos los productos analizados
  @GetMapping
  public ResponseEntity<LiquidacionResponse> obtenerLiquidacion() {
    log.info("Solicitando análisis de liquidación completo");

    List<LiquidacionProducto> liquidaciones =
            liquidacionService.calcularLiquidacion();

    LiquidacionService.ResumenLiquidacion resumen =
            liquidacionService.calcularResumen(liquidaciones);

    List<LiquidacionItemResponse> items = liquidaciones.stream()
            .map(mapper::toItemResponse)
            .toList();

    LiquidacionResumenResponse resumenResponse =
            mapper.toResumenResponse(resumen);

    return ResponseEntity.ok(
            new LiquidacionResponse(resumenResponse, items));
  }

  // GET /api/liquidacion/congelados
  // Solo productos congelados (>8 ferias sin vender)
  @GetMapping("/congelados")
  public ResponseEntity<List<LiquidacionItemResponse>> obtenerCongelados() {
    List<LiquidacionItemResponse> congelados =
            liquidacionService.calcularLiquidacion().stream()
                    .filter(l -> "CONGELADO".equals(l.getEstado()))
                    .map(mapper::toItemResponse)
                    .toList();

    return ResponseEntity.ok(congelados);
  }

  // GET /api/liquidacion/lentos
  // Solo productos lentos (4-8 ferias sin vender)
  @GetMapping("/lentos")
  public ResponseEntity<List<LiquidacionItemResponse>> obtenerLentos() {
    List<LiquidacionItemResponse> lentos =
            liquidacionService.calcularLiquidacion().stream()
                    .filter(l -> "LENTO".equals(l.getEstado()))
                    .map(mapper::toItemResponse)
                    .toList();

    return ResponseEntity.ok(lentos);
  }

  // GET /api/liquidacion/resumen
  // Solo el resumen financiero
  @GetMapping("/resumen")
  public ResponseEntity<LiquidacionResumenResponse> obtenerResumen() {
    List<LiquidacionProducto> liquidaciones =
            liquidacionService.calcularLiquidacion();

    LiquidacionService.ResumenLiquidacion resumen =
            liquidacionService.calcularResumen(liquidaciones);

    return ResponseEntity.ok(mapper.toResumenResponse(resumen));
  }
}
