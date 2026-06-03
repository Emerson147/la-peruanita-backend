package com.emersondev.application.usecase.reportes;

import com.emersondev.domain.model.*;
import com.emersondev.domain.model.ReporteVentas.*;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.domain.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObtenerReporteUseCase {

  private final VentaRepository ventaRepository;
  private final ProductoRepository productoRepository;

  public ReporteVentas ejecutar(
          LocalDateTime desde, LocalDateTime hasta,
          String periodo) {

    log.info("Generando reporte desde {} hasta {}", desde, hasta);

    List<Venta> ventas = ventaRepository
            .findByStatus("completed").stream()
            .filter(v -> v.getCreatedAt() != null
                    && !v.getCreatedAt().isBefore(desde)
                    && !v.getCreatedAt().isAfter(hasta))
            .toList();

    List<Producto> productos = productoRepository.findAll();

    Map<UUID, BigDecimal> costos = productos.stream()
            .collect(Collectors.toMap(
                    Producto::getId,
                    p -> p.getCost() != null
                            ? p.getCost() : BigDecimal.ZERO,
                    (a, b) -> a));

    Map<UUID, String> categorias = productos.stream()
            .collect(Collectors.toMap(
                    Producto::getId,
                    p -> p.getCategoria() != null
                            ? p.getCategoria() : "Sin categoría",
                    (a, b) -> a));

    ReporteVentas reporte = new ReporteVentas();
    reporte.setDesde(desde);
    reporte.setHasta(hasta);
    reporte.setPeriodo(periodo);

    // RESUMEN EJECUTIVO
    calcularResumen(reporte, ventas, costos);

    // VENTAS POR DÍA
    reporte.setVentasPorDia(calcularVentasPorDia(ventas));

    // FERIAS VS TIENDA
    calcularFeriasVsTienda(reporte, ventas, costos);

    // TOP PRODUCTOS
    reporte.setTopProductos(
            calcularTopProductos(ventas, costos, categorias, 10));

    // DISTRIBUCIÓN POR CATEGORÍA
    reporte.setVentasPorCategoria(
            calcularPorCategoria(ventas, categorias));

    // VENTAS POR VENDEDOR
    reporte.setVentasPorVendedor(
            calcularPorVendedor(ventas));

    reporte.setNombreFeriaJueves("Acobamba");
    reporte.setNombreFeriaDomingo("Paucara");

    // ANÁLISIS ABC
    calcularABC(reporte, ventas, costos, categorias);

    // TENDENCIA FERIAS
    calcularTendenciaFerias(reporte);

    log.info("Reporte generado con {} ventas", ventas.size());
    return reporte;
  }

  // =============================================
  // RESUMEN EJECUTIVO
  // =============================================

  private void calcularResumen(ReporteVentas reporte,
                               List<Venta> ventas,
                               Map<UUID, BigDecimal> costos) {

    BigDecimal ingresos = ventas.stream()
            .map(Venta::getTotal)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal ganancia = calcularGananciaTotal(ventas, costos);

    int unidades = ventas.stream()
            .filter(v -> v.getItems() != null)
            .flatMap(v -> v.getItems().stream())
            .mapToInt(VentaItem::getQuantity)
            .sum();

    BigDecimal ticket = ventas.isEmpty()
            ? BigDecimal.ZERO
            : ingresos.divide(
            BigDecimal.valueOf(ventas.size()),
            2, RoundingMode.HALF_UP);

    reporte.setIngresosTotales(ingresos);
    reporte.setGananciaNeta(ganancia);
    reporte.setProductosVendidos(unidades);
    reporte.setTotalVentas(ventas.size());
    reporte.setTicketPromedio(ticket);

    // Crecimiento vs semana anterior
    LocalDateTime inicioSemanaAnterior =
            reporte.getDesde().minusWeeks(1);
    LocalDateTime finSemanaAnterior = reporte.getDesde();

    List<Venta> ventasSemanaAnterior = ventaRepository
            .findByStatus("completed").stream()
            .filter(v -> v.getCreatedAt() != null
                    && !v.getCreatedAt().isBefore(
                    inicioSemanaAnterior)
                    && v.getCreatedAt().isBefore(
                    finSemanaAnterior))
            .toList();

    BigDecimal ingresosSemanaAnterior =
            ventasSemanaAnterior.stream()
                    .map(Venta::getTotal)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (ingresosSemanaAnterior.compareTo(BigDecimal.ZERO) > 0) {
      BigDecimal crecimiento = ingresos
              .subtract(ingresosSemanaAnterior)
              .divide(ingresosSemanaAnterior, 4,
                      RoundingMode.HALF_UP)
              .multiply(BigDecimal.valueOf(100))
              .setScale(1, RoundingMode.HALF_UP);
      reporte.setCrecimientoSemanal(crecimiento);
    } else {
      reporte.setCrecimientoSemanal(BigDecimal.ZERO);
    }
  }

  // =============================================
  // FERIAS VS TIENDA
  // =============================================

  private void calcularFeriasVsTienda(ReporteVentas reporte,
                                      List<Venta> ventas,
                                      Map<UUID, BigDecimal> costos) {

    // Ferias = jueves y domingo
    List<Venta> ventasFerias = ventas.stream()
            .filter(v -> {
              DayOfWeek dia = v.getCreatedAt()
                      .getDayOfWeek();
              return dia == DayOfWeek.THURSDAY
                      || dia == DayOfWeek.SUNDAY;
            }).toList();

    List<Venta> ventasTienda = ventas.stream()
            .filter(v -> {
              DayOfWeek dia = v.getCreatedAt()
                      .getDayOfWeek();
              return dia != DayOfWeek.THURSDAY
                      && dia != DayOfWeek.SUNDAY;
            }).toList();

    BigDecimal ingresosFerias = ventasFerias.stream()
            .map(Venta::getTotal)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal ingresosTienda = ventasTienda.stream()
            .map(Venta::getTotal)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    reporte.setVentasFerias(ingresosFerias);
    reporte.setGananciaFerias(
            calcularGananciaTotal(ventasFerias, costos));
    reporte.setVentasTienda(ingresosTienda);
    reporte.setGananciaTienda(
            calcularGananciaTotal(ventasTienda, costos));

    // Mejor feria — día con más ingresos entre jueves y domingo
    Map<String, BigDecimal> ingresosPorFeria =
            new LinkedHashMap<>();
    ingresosPorFeria.put("Jueves", BigDecimal.ZERO);
    ingresosPorFeria.put("Domingo", BigDecimal.ZERO);

    ventasFerias.forEach(v -> {
      String dia = v.getCreatedAt().getDayOfWeek()
              == DayOfWeek.THURSDAY ? "Jueves" : "Domingo";
      ingresosPorFeria.merge(dia,
              v.getTotal() != null
                      ? v.getTotal() : BigDecimal.ZERO,
              BigDecimal::add);
    });

    ingresosPorFeria.entrySet().stream()
            .filter(e -> e.getValue().compareTo(BigDecimal.ZERO) > 0)
            .max(Map.Entry.comparingByValue())
            .ifPresentOrElse(
                    e -> {
                      reporte.setMejorFeria(e.getKey());
                      reporte.setIngresosMejorFeria(e.getValue());
                    },
                    () -> {
                      reporte.setMejorFeria("Sin ventas en ferias");
                      reporte.setIngresosMejorFeria(BigDecimal.ZERO);
                    });
  }

  // =============================================
  // TOP PRODUCTOS
  // =============================================

  private List<ProductoReporte> calcularTopProductos(
          List<Venta> ventas,
          Map<UUID, BigDecimal> costos,
          Map<UUID, String> categorias,
          int limite) {

    Map<UUID, int[]> stats = new HashMap<>();

    ventas.stream()
            .filter(v -> v.getItems() != null)
            .flatMap(v -> v.getItems().stream())
            .forEach(item -> stats.merge(
                    item.getProductId(),
                    new int[]{item.getQuantity(),
                            item.getSubtotal().intValue()},
                    (a, b) -> new int[]{
                            a[0] + b[0], a[1] + b[1]}));

    return stats.entrySet().stream()
            .map(entry -> {
              UUID id = entry.getKey();
              BigDecimal costo = costos.getOrDefault(
                      id, BigDecimal.ZERO);
              BigDecimal ingresos = BigDecimal.valueOf(
                      entry.getValue()[1]);
              int unidades = entry.getValue()[0];

              // Margen = (ingresos - costo*unidades) / ingresos
              BigDecimal costoTotal = costo.multiply(
                      BigDecimal.valueOf(unidades));
              BigDecimal margen = ingresos.compareTo(
                      BigDecimal.ZERO) > 0
                      ? ingresos.subtract(costoTotal)
                      .divide(ingresos, 4,
                              RoundingMode.HALF_UP)
                      .multiply(BigDecimal.valueOf(100))
                      .setScale(1, RoundingMode.HALF_UP)
                      : BigDecimal.ZERO;

              // Buscar nombre del producto
              String nombre = ventas.stream()
                      .filter(v -> v.getItems() != null)
                      .flatMap(v -> v.getItems().stream())
                      .filter(i -> id.equals(i.getProductId()))
                      .map(VentaItem::getProductName)
                      .findFirst()
                      .orElse("Desconocido");

              return new ProductoReporte(
                      nombre,
                      categorias.getOrDefault(id, ""),
                      unidades,
                      ingresos,
                      margen);
            })
            .sorted(Comparator.comparing(
                    ProductoReporte::ingresos).reversed())
            .limit(limite)
            .toList();
  }

  // =============================================
  // DISTRIBUCIÓN POR CATEGORÍA
  // =============================================

  private Map<String, BigDecimal> calcularPorCategoria(
          List<Venta> ventas,
          Map<UUID, String> categorias) {

    Map<String, BigDecimal> resultado = new LinkedHashMap<>();

    ventas.stream()
            .filter(v -> v.getItems() != null)
            .flatMap(v -> v.getItems().stream())
            .forEach(item -> {
              String cat = categorias.getOrDefault(
                      item.getProductId(), "Sin categoría");
              resultado.merge(cat,
                      item.getSubtotal() != null
                              ? item.getSubtotal()
                              : BigDecimal.ZERO,
                      BigDecimal::add);
            });

    return resultado.entrySet().stream()
            .sorted(Map.Entry.<String, BigDecimal>
                    comparingByValue().reversed())
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (a, b) -> a,
                    LinkedHashMap::new));
  }

  // =============================================
  // VENTAS POR VENDEDOR
  // =============================================

  private List<VendedorReporte> calcularPorVendedor(
          List<Venta> ventas) {

    BigDecimal totalGeneral = ventas.stream()
            .map(Venta::getTotal)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    Map<String, List<Venta>> porVendedor = ventas.stream()
            .filter(v -> v.getCreatedBy() != null)
            .collect(Collectors.groupingBy(
                    Venta::getCreatedBy));

    return porVendedor.entrySet().stream()
            .map(entry -> {
              List<Venta> ventasVendedor = entry.getValue();
              BigDecimal ingresos = ventasVendedor.stream()
                      .map(Venta::getTotal)
                      .filter(Objects::nonNull)
                      .reduce(BigDecimal.ZERO,
                              BigDecimal::add);

              BigDecimal ticket = ventasVendedor.isEmpty()
                      ? BigDecimal.ZERO
                      : ingresos.divide(
                      BigDecimal.valueOf(
                              ventasVendedor.size()),
                      2, RoundingMode.HALF_UP);

              double participacion = totalGeneral.compareTo(
                      BigDecimal.ZERO) > 0
                      ? ingresos.divide(totalGeneral, 4,
                              RoundingMode.HALF_UP)
                      .multiply(BigDecimal.valueOf(100))
                      .doubleValue()
                      : 0.0;

              return new VendedorReporte(
                      entry.getKey(),
                      ventasVendedor.size(),
                      ingresos,
                      ticket,
                      Math.round(participacion * 10.0) / 10.0);
            })
            .sorted(Comparator.comparing(
                    VendedorReporte::totalIngresos).reversed())
            .toList();
  }

  // =============================================
  // ANÁLISIS ABC
  // =============================================

  private void calcularABC(ReporteVentas reporte,
                           List<Venta> ventas,
                           Map<UUID, BigDecimal> costos,
                           Map<UUID, String> categorias) {

    List<ProductoReporte> todos = calcularTopProductos(
            ventas, costos, categorias, Integer.MAX_VALUE);

    BigDecimal totalIngresos = todos.stream()
            .map(ProductoReporte::ingresos)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (totalIngresos.compareTo(BigDecimal.ZERO) == 0) {
      reporte.setProductosA(List.of());
      reporte.setProductosB(List.of());
      reporte.setProductosC(List.of());
      return;
    }

    List<ProductoABC> productosABC = new ArrayList<>();
    BigDecimal acumulado = BigDecimal.ZERO;

    for (ProductoReporte p : todos) {
      double porcentaje = p.ingresos()
              .divide(totalIngresos, 4, RoundingMode.HALF_UP)
              .multiply(BigDecimal.valueOf(100))
              .doubleValue();

//    Clasificar antes de acumular
//    acumulado = acumulado.add(p.ingresos());
      double porcentajeAcumuladoAntes = acumulado
              .divide(totalIngresos, 4, RoundingMode.HALF_UP)
              .multiply(BigDecimal.valueOf(100))
              .doubleValue();

      // A = primeros productos que suman 80%
      // B = siguientes que suman hasta 95%
      // C = el resto
      String clasificacion;
      if (porcentajeAcumuladoAntes < 80) {
        clasificacion = "A";
      } else if (porcentajeAcumuladoAntes < 95) {
        clasificacion = "B";
      } else {
        clasificacion = "C";
      }

      acumulado = acumulado.add(p.ingresos());

      productosABC.add(new ProductoABC(
              p.nombre(),
              p.categoria(),
              p.unidadesVendidas(),
              p.ingresos(),
              Math.round(porcentaje * 10.0) / 10.0,
              clasificacion));
    }

    reporte.setProductosA(productosABC.stream()
            .filter(p -> "A".equals(p.clasificacion()))
            .toList());
    reporte.setProductosB(productosABC.stream()
            .filter(p -> "B".equals(p.clasificacion()))
            .toList());
    reporte.setProductosC(productosABC.stream()
            .filter(p -> "C".equals(p.clasificacion()))
            .toList());
  }

  // =============================================
  // TENDENCIA FERIAS — PROMEDIO MÓVIL
  // =============================================

  private void calcularTendenciaFerias(ReporteVentas reporte) {

    LocalDateTime ahora = LocalDateTime.now();
    List<Venta> todasLasVentas = ventaRepository
            .findByStatus("completed");

    // Promedio móvil últimas 4 ferias de jueves
    BigDecimal promedioJueves = calcularPromedioFeria(
            todasLasVentas, DayOfWeek.THURSDAY, 4, ahora);

    // Promedio móvil últimas 4 ferias de domingo
    BigDecimal promedioDomingo = calcularPromedioFeria(
            todasLasVentas, DayOfWeek.SUNDAY, 4, ahora);

    reporte.setPromedioMovilJueves(promedioJueves);
    reporte.setPromedioMovilDomingo(promedioDomingo);

    // Próxima feria
    DayOfWeek hoy = ahora.getDayOfWeek();
    boolean proximaEsJueves = hoy.getValue() < 4
            || hoy == DayOfWeek.SUNDAY
            || hoy == DayOfWeek.MONDAY
            || hoy == DayOfWeek.TUESDAY
            || hoy == DayOfWeek.WEDNESDAY;

    if (proximaEsJueves) {
      reporte.setProximaFeria("Jueves");
      reporte.setPrediccionProximaFeria(promedioJueves);
    } else {
      reporte.setProximaFeria("Domingo");
      reporte.setPrediccionProximaFeria(promedioDomingo);
    }
  }

  private BigDecimal calcularPromedioFeria(
          List<Venta> ventas, DayOfWeek dia,
          int ultimas, LocalDateTime ahora) {

    // Agrupar ventas por semana del día específico
    Map<String, BigDecimal> ventasPorFeria = new LinkedHashMap<>();

    ventas.stream()
            .filter(v -> v.getCreatedAt() != null
                    && v.getCreatedAt().getDayOfWeek() == dia)
            .forEach(v -> {
              String semana = v.getCreatedAt()
                      .toLocalDate().toString();
              ventasPorFeria.merge(semana,
                      v.getTotal() != null
                              ? v.getTotal() : BigDecimal.ZERO,
                      BigDecimal::add);
            });

    // Tomar las últimas N ferias
    List<BigDecimal> ultimas4 = ventasPorFeria.values()
            .stream()
            .sorted(Comparator.reverseOrder())
            .limit(ultimas)
            .toList();

    if (ultimas4.isEmpty()) return BigDecimal.ZERO;

    BigDecimal suma = ultimas4.stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return suma.divide(
            BigDecimal.valueOf(ultimas4.size()),
            2, RoundingMode.HALF_UP);
  }

  // =============================================
  // VENTAS POR DÍA
  // =============================================

  private Map<String, BigDecimal> calcularVentasPorDia(
          List<Venta> ventas) {

    Map<String, BigDecimal> resultado = new LinkedHashMap<>();
    resultado.put("Lunes", BigDecimal.ZERO);
    resultado.put("Martes", BigDecimal.ZERO);
    resultado.put("Miércoles", BigDecimal.ZERO);
    resultado.put("Jueves", BigDecimal.ZERO);
    resultado.put("Viernes", BigDecimal.ZERO);
    resultado.put("Sábado", BigDecimal.ZERO);
    resultado.put("Domingo", BigDecimal.ZERO);

    ventas.forEach(v -> {
      if (v.getCreatedAt() != null) {
        String dia = v.getCreatedAt().getDayOfWeek()
                .getDisplayName(TextStyle.FULL,
                        Locale.forLanguageTag("es-PE"));
        String diaCapitalizado = dia.substring(0, 1)
                .toUpperCase() + dia.substring(1);
        resultado.merge(diaCapitalizado,
                v.getTotal() != null
                        ? v.getTotal() : BigDecimal.ZERO,
                BigDecimal::add);
      }
    });

    return resultado;
  }

  // =============================================
  // HELPER — GANANCIA TOTAL
  // =============================================

  private BigDecimal calcularGananciaTotal(
          List<Venta> ventas,
          Map<UUID, BigDecimal> costos) {

    return ventas.stream()
            .filter(v -> v.getItems() != null)
            .flatMap(v -> v.getItems().stream())
            .map(item -> {
              BigDecimal costo = costos.getOrDefault(
                      item.getProductId(), BigDecimal.ZERO);
              return item.getUnitPrice()
                      .subtract(costo)
                      .multiply(BigDecimal.valueOf(
                              item.getQuantity()));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}