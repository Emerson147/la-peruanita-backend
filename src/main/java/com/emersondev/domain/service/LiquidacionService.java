package com.emersondev.domain.service;

import com.emersondev.domain.model.Producto;
import com.emersondev.domain.model.LiquidacionProducto;
import com.emersondev.domain.model.Venta;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.domain.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiquidacionService {

  private final ProductoRepository productoRepository;
  private final VentaRepository ventaRepository;
  private final List<EstrategiaLiquidacion> estrategias;

  // =============================================
  // CONFIGURACIÓN DEL MODELO DE FERIAS DE DENRAF
  // Jueves y Domingo — 2 ferias por semana
  // =============================================
  private static final double DIAS_POR_FERIA = 3.5;
  private static final int FERIAS_UMBRAL_CONGELADO = 8;  // >8 ferias = congelado
  private static final int FERIAS_UMBRAL_LENTO = 4;      // 4-8 ferias = lento

  public List<LiquidacionProducto> calcularLiquidacion() {
    log.info("Calculando liquidación urgente — modelo ferias DenRaf");

    List<Producto> productos = productoRepository.findByStatus("active");

    List<LiquidacionProducto> liquidaciones = productos.stream()
            .map(this::procesarProducto)
            .sorted(Comparator.comparingInt(
                    LiquidacionProducto::getFeriasSinVender).reversed())
            .toList();

    log.info("Liquidación calculada: {} productos procesados",
            liquidaciones.size());
    return liquidaciones;
  }

  private LiquidacionProducto procesarProducto(Producto producto) {
    LiquidacionProducto liquidacion = new LiquidacionProducto();
    liquidacion.setProducto(producto);

    // Días y ferias sin vender
    int dias = calcularDiasSinVender(producto);
    int ferias = calcularFeriasSinVender(dias);

    liquidacion.setDiasSinVender(dias);
    liquidacion.setFeriasSinVender(ferias);

    // Rotación por feria (ventas último mes / 8 ferias)
    double rotacionPorFeria = calcularRotacionPorFeria(producto);
    liquidacion.setRotacionPorFeria(rotacionPorFeria);

    // Calcular todos los precios
    liquidacion.calcularPrecios();

    // Clasificar con Strategy Pattern
    estrategias.stream()
            .filter(e -> e.aplica(ferias))
            .findFirst()
            .ifPresent(estrategia -> {
              log.debug("Estrategia '{}' → producto: {}",
                      estrategia.nombre(), producto.getName());
              estrategia.aplicar(liquidacion);
            });

    return liquidacion;
  }

  // =============================================
  // CÁLCULO DE DÍAS SIN VENDER
  // Basado en última venta real o fecha de creación
  // =============================================
  private int calcularDiasSinVender(Producto producto) {
    List<Venta> ventas = ventaRepository.findAll();

    Optional<LocalDateTime> ultimaVenta = ventas.stream()
            .filter(v -> "completed".equals(v.getStatus()))
            .filter(v -> v.getItems() != null)
            .filter(v -> v.getItems().stream()
                    .anyMatch(item -> item.getProductId()
                            .equals(producto.getId())))
            .map(Venta::getCreatedAt)
            .max(Comparator.naturalOrder());

    LocalDateTime referencia = ultimaVenta
            .orElse(producto.getCreatedAt());

    return (int) ChronoUnit.DAYS.between(
            referencia, LocalDateTime.now());
  }

  // =============================================
  // CÁLCULO DE FERIAS SIN VENDER
  // Cuenta jueves y domingos reales transcurridos
  // =============================================
  int calcularFeriasSinVender(int diasSinVender) {
    if (diasSinVender <= 0) return 0;

    LocalDate desde = LocalDate.now().minusDays(diasSinVender);
    LocalDate hasta = LocalDate.now();
    int ferias = 0;

    LocalDate fecha = desde;
    while (!fecha.isAfter(hasta)) {
      DayOfWeek dia = fecha.getDayOfWeek();
      // Solo cuenta jueves y domingos — los días de feria de DenRaf
      if (dia == DayOfWeek.THURSDAY || dia == DayOfWeek.SUNDAY) {
        ferias++;
      }
      fecha = fecha.plusDays(1);
    }

    return ferias;
  }

  // =============================================
  // ROTACIÓN POR FERIA
  // Cuántas unidades se venden por feria en promedio
  // =============================================
  private double calcularRotacionPorFeria(Producto producto) {
    LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
    List<Venta> ventas = ventaRepository.findAll();

    int totalVendido = ventas.stream()
            .filter(v -> "completed".equals(v.getStatus()))
            .filter(v -> v.getCreatedAt() != null &&
                    v.getCreatedAt().isAfter(hace30Dias))
            .filter(v -> v.getItems() != null)
            .flatMap(v -> v.getItems().stream())
            .filter(item -> item.getProductId().equals(producto.getId()))
            .mapToInt(item -> item.getQuantity())
            .sum();

    // 8 ferias en 30 días (2 por semana)
    return (double) totalVendido / 8;
  }

  // =============================================
  // RESUMEN FINANCIERO DEL INVENTARIO
  // =============================================
  public ResumenLiquidacion calcularResumen(
          List<LiquidacionProducto> liquidaciones) {

    BigDecimal totalCapital = liquidaciones.stream()
            .map(LiquidacionProducto::getCapitalCongelado)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal capitalCongelado = liquidaciones.stream()
            .filter(l -> "CONGELADO".equals(l.getEstado()))
            .map(LiquidacionProducto::getCapitalCongelado)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal capitalLento = liquidaciones.stream()
            .filter(l -> "LENTO".equals(l.getEstado()))
            .map(LiquidacionProducto::getCapitalCongelado)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal capitalActivo = liquidaciones.stream()
            .filter(l -> "ACTIVO".equals(l.getEstado()))
            .map(LiquidacionProducto::getCapitalCongelado)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Meta: liberar 50% del capital congelado
    BigDecimal metaLiberacion = capitalCongelado
            .multiply(BigDecimal.valueOf(0.5))
            .setScale(2, RoundingMode.HALF_UP);

    // Ratio de liquidez — % del capital que está congelado
    BigDecimal ratioLiquidez = totalCapital.compareTo(BigDecimal.ZERO) > 0
            ? capitalCongelado
            .divide(totalCapital, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

    long productosCongelados = liquidaciones.stream()
            .filter(l -> "CONGELADO".equals(l.getEstado())).count();

    long productosLentos = liquidaciones.stream()
            .filter(l -> "LENTO".equals(l.getEstado())).count();

    long productosActivos = liquidaciones.stream()
            .filter(l -> "ACTIVO".equals(l.getEstado())).count();

    return new ResumenLiquidacion(
            totalCapital,
            capitalActivo,
            capitalLento,
            capitalCongelado,
            metaLiberacion,
            ratioLiquidez,
            productosCongelados,
            productosLentos,
            productosActivos
    );
  }

  public record ResumenLiquidacion(
          BigDecimal totalCapital,
          BigDecimal capitalActivo,
          BigDecimal capitalLento,
          BigDecimal capitalCongelado,
          BigDecimal metaLiberacion,
          BigDecimal ratioLiquidez,
          Long productosCongelados,
          Long productosLentos,
          Long productosActivos
  ) {}
}