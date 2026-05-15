package com.emersondev.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ReporteVentas {

  // Resumen ejecutivo
  private BigDecimal ingresosTotales;
  private BigDecimal gananciaNeta;
  private Integer productosVendidos;
  private Integer totalVentas;
  private BigDecimal ticketPromedio;
  private BigDecimal crecimientoSemanal;

  // Ventas por período
  private Map<String, BigDecimal> ventasPorDia;
  private Map<String, BigDecimal> ventasPorSemana;

  // Ferias vs Tienda
  private String nombreFeriaJueves; // Acobamba
  private String nombreFeriaDomingo; // Paucara
  private BigDecimal ventasFerias;    // jueves + domingo
  private BigDecimal gananciaFerias;
  private BigDecimal ventasTienda;    // lunes a sábado
  private BigDecimal gananciaTienda;
  private String mejorFeria;
  private BigDecimal ingresosMejorFeria;


  // Top productos
  private List<ProductoReporte> topProductos;

  // Distribución por categoría
  private Map<String, BigDecimal> ventasPorCategoria;

  // Ventas por vendedor
  private List<VendedorReporte> ventasPorVendedor;

  // Análisis ABC
  private List<ProductoABC> productosA; // 80% ingresos
  private List<ProductoABC> productosB; // 15% ingresos
  private List<ProductoABC> productosC; // 5% ingresos

  // Tendencia ferias
  private BigDecimal promedioMovilJueves;
  private BigDecimal promedioMovilDomingo;
  private String proximaFeria;
  private BigDecimal prediccionProximaFeria;

  // Período del reporte
  private LocalDateTime desde;
  private LocalDateTime hasta;
  private String periodo;

  // Records internos
  public record ProductoReporte(
          String nombre,
          String categoria,
          Integer unidadesVendidas,
          BigDecimal ingresos,
          BigDecimal margen) {}

  public record VendedorReporte(
          String nombre,
          Integer totalVentas,
          BigDecimal totalIngresos,
          BigDecimal ticketPromedio,
          Double participacion) {}

  public record ProductoABC(
          String nombre,
          String categoria,
          Integer unidades,
          BigDecimal ingresos,
          Double porcentajeDelTotal,
          String clasificacion) {}

}