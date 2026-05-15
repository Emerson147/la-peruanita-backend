package com.emersondev.domain.model;

import com.emersondev.application.usecase.dashboard.ObtenerDashboardUseCase.ProductoRentable;
import com.emersondev.application.usecase.dashboard.ObtenerDashboardUseCase.ProyeccionSemana;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class Dashboard {

  // Resumen ejecutivo
  private Integer ventasHoy;
  private BigDecimal ingresosHoy;
  private BigDecimal ingresosSemana;
  private BigDecimal gananciaNeta;
  private BigDecimal inversionTotal;
  private String mejorDiaSemana;
  private BigDecimal ingresosMejorDia;

  // Análisis avanzado
  private BigDecimal margenRentabilidad;
  private BigDecimal roi;
  private BigDecimal ticketPromedio;

  // Comparativa
  private BigDecimal ingresosSemanaAnterior;
  private BigDecimal crecimientoSemanal;

  // Top productos
  private List<TopProducto> topProductos;

  // Ventas por día de la semana
  private Map<String, BigDecimal> ventasPorDia;

  private List<Venta> actividadReciente;
  private List<ProductoRentable> productosRentables;
  private List<Producto> productosStockBajo;
  private ProyeccionSemana proyeccion;
  private BigDecimal conversion;

  // Clase interna para top productos
  public record TopProducto(
          String nombre,
          String categoria,
          Integer unidadesVendidas,
          BigDecimal ingresos
  ) {}

}
