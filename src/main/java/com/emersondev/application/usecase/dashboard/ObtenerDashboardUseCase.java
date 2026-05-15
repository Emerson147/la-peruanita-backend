package com.emersondev.application.usecase.dashboard;

import com.emersondev.domain.model.Dashboard;
import com.emersondev.domain.model.Producto;
import com.emersondev.domain.model.Venta;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.domain.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ObtenerDashboardUseCase {

  private final VentaRepository ventaRepository;
  private final ProductoRepository productoRepository;

  public Dashboard ejecutar(String periodo) {
    log.info("Calculando dashboard para periodo: {}", periodo);

    ZoneId zonaPeru =  ZoneId.of("America/Lima");

    LocalDateTime ahora = LocalDateTime.now(zonaPeru);
    LocalDateTime inicioSemana = ahora.with(DayOfWeek.MONDAY)
            .withHour(0).withMinute(0).withSecond(0);
    LocalDateTime inicioSemanaAnterior = inicioSemana.minusWeeks(1);
    LocalDateTime finSemanaAnterior = inicioSemana.minusSeconds(1);
    LocalDateTime inicioDia = ahora.withHour(0)
            .withMinute(0).withSecond(0);

    // Traer todas las ventas completadas
    List<Venta> todasLasVentas = ventaRepository.findByStatus("completed");

    // Filtrar por periodos
    List<Venta> ventasHoy = filtrarPorFecha(
            todasLasVentas, inicioDia, ahora);
    List<Venta> ventasSemana = filtrarPorFecha(
            todasLasVentas, inicioSemana, ahora);
    List<Venta> ventasSemanaAnterior = filtrarPorFecha(
            todasLasVentas, inicioSemanaAnterior, finSemanaAnterior);

    // Traer productos para calcular inversión
    List<Producto> productos = productoRepository.findAll();

    Dashboard dashboard = new Dashboard();

    // RESUMEN EJECUTIVO
    dashboard.setVentasHoy(ventasHoy.size());
    dashboard.setIngresosHoy(calcularIngresos(ventasHoy));
    dashboard.setIngresosSemana(calcularIngresos(ventasSemana));
    dashboard.setGananciaNeta(calcularGanancia(
            ventasSemana, productos));
    dashboard.setInversionTotal(calcularInversion(productos));

    // Mejor día de la semana
    calcularMejorDia(ventasSemana, dashboard);

    // ANÁLISIS AVANZADO
    BigDecimal ingresos = calcularIngresos(ventasSemana);
    BigDecimal inversion = calcularInversion(productos);
    BigDecimal ganancia = calcularGanancia(ventasSemana, productos);

    // Margen = (ganancia / ingresos) * 100
    if (ingresos.compareTo(BigDecimal.ZERO) > 0) {
      dashboard.setMargenRentabilidad(
              ganancia.divide(ingresos, 4, RoundingMode.HALF_UP)
                      .multiply(BigDecimal.valueOf(100))
                      .setScale(2, RoundingMode.HALF_UP));
    } else {
      dashboard.setMargenRentabilidad(BigDecimal.ZERO);
    }

    // ROI = (ganancia / inversión) * 100
    if (inversion.compareTo(BigDecimal.ZERO) > 0) {
      dashboard.setRoi(
              ganancia.divide(inversion, 4, RoundingMode.HALF_UP)
                      .multiply(BigDecimal.valueOf(100))
                      .setScale(2, RoundingMode.HALF_UP));
    } else {
      dashboard.setRoi(BigDecimal.ZERO);
    }

    // Ticket promedio = ingresos / cantidad de ventas
    if (!ventasSemana.isEmpty()) {
      dashboard.setTicketPromedio(
              ingresos.divide(
                      BigDecimal.valueOf(ventasSemana.size()),
                      2, RoundingMode.HALF_UP));
    } else {
      dashboard.setTicketPromedio(BigDecimal.ZERO);
    }

    // COMPARATIVA SEMANAL
    BigDecimal ingresosSemanaAnterior = calcularIngresos(
            ventasSemanaAnterior);
    dashboard.setIngresosSemanaAnterior(ingresosSemanaAnterior);

    // Crecimiento = ((semana actual - semana anterior) / semana anterior) * 100
    if (ingresosSemanaAnterior.compareTo(BigDecimal.ZERO) > 0) {
      dashboard.setCrecimientoSemanal(
              ingresos.subtract(ingresosSemanaAnterior)
                      .divide(ingresosSemanaAnterior, 4, RoundingMode.HALF_UP)
                      .multiply(BigDecimal.valueOf(100))
                      .setScale(2, RoundingMode.HALF_UP));
    } else {
      dashboard.setCrecimientoSemanal(BigDecimal.ZERO);
    }

    // TOP PRODUCTOS
    dashboard.setTopProductos(calcularTopProductos(
            ventasSemana, productos, 5));

    // VENTAS POR DÍA
    dashboard.setVentasPorDia(calcularVentasPorDia(ventasSemana));

    log.info("Dashboard calculado exitosamente");

    // ACTIVIDAD RECIENTE — últimas 10 ventas
    dashboard.setActividadReciente(
            obtenerActividadReciente(10));

// PRODUCTOS MÁS RENTABLES
    dashboard.setProductosRentables(
            obtenerProductosRentables(ventasSemana, productos, 5));

// STOCK BAJO
    dashboard.setProductosStockBajo(
            obtenerProductosStockBajo());

// PROYECCIÓN
    dashboard.setProyeccion(
            calcularProyeccion(todasLasVentas));

// CONVERSIÓN = ventas completadas / total ventas * 100
    List<Venta> todasSemana = ventaRepository
            .findByFecha(inicioSemana, ahora);
    if (!todasSemana.isEmpty()) {
      dashboard.setConversion(
              BigDecimal.valueOf(ventasSemana.size())
                      .divide(BigDecimal.valueOf(todasSemana.size()),
                              4, RoundingMode.HALF_UP)
                      .multiply(BigDecimal.valueOf(100))
                      .setScale(1, RoundingMode.HALF_UP));
    } else {
      dashboard.setConversion(BigDecimal.ZERO);
    }

    return dashboard;
  }

  // =============================================
  // MÉTODOS PRIVADOS DE CÁLCULO
  // =============================================

  private List<Venta> filtrarPorFecha(List<Venta> ventas,
                                      LocalDateTime desde, LocalDateTime hasta) {
    return ventas.stream()
            .filter(v -> v.getCreatedAt() != null
                    && !v.getCreatedAt().isBefore(desde)
                    && !v.getCreatedAt().isAfter(hasta))
            .toList();
  }

  private BigDecimal calcularIngresos(List<Venta> ventas) {
    return ventas.stream()
            .map(Venta::getTotal)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private BigDecimal calcularGanancia(List<Venta> ventas,
                                      List<Producto> productos) {

    // Mapa de producto id → costo
    Map<UUID, BigDecimal> costos = productos.stream()
            .collect(Collectors.toMap(
                    Producto::getId,
                    Producto::getCost,
                    (a, b) -> a));

    return ventas.stream()
            .flatMap(v -> v.getItems() != null
                    ? v.getItems().stream() : java.util.stream.Stream.empty())
            .map(item -> {
              BigDecimal costo = costos.getOrDefault(
                      item.getProductId(), BigDecimal.ZERO);
              BigDecimal gananciaItem = item.getUnitPrice()
                      .subtract(costo)
                      .multiply(BigDecimal.valueOf(item.getQuantity()));
              return gananciaItem;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private BigDecimal calcularInversion(List<Producto> productos) {
    return productos.stream()
            .map(p -> p.getCost().multiply(
                    BigDecimal.valueOf(p.getStock())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private void calcularMejorDia(List<Venta> ventas, Dashboard dashboard) {
    Map<String, BigDecimal> ingresosPorDia = calcularVentasPorDia(ventas);

    ingresosPorDia.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .ifPresent(entry -> {
              dashboard.setMejorDiaSemana(entry.getKey());
              dashboard.setIngresosMejorDia(entry.getValue());
            });

    if (dashboard.getMejorDiaSemana() == null) {
      dashboard.setMejorDiaSemana("Sin ventas");
      dashboard.setIngresosMejorDia(BigDecimal.ZERO);
    }
  }

  private Map<String, BigDecimal> calcularVentasPorDia(
          List<Venta> ventas) {

    Map<String, BigDecimal> resultado = new LinkedHashMap<>();

    // Inicializar todos los días en 0
    resultado.put("Lunes", BigDecimal.ZERO);
    resultado.put("Martes", BigDecimal.ZERO);
    resultado.put("Miércoles", BigDecimal.ZERO);
    resultado.put("Jueves", BigDecimal.ZERO);
    resultado.put("Viernes", BigDecimal.ZERO);
    resultado.put("Sábado", BigDecimal.ZERO);
    resultado.put("Domingo", BigDecimal.ZERO);

    ventas.forEach(venta -> {
      if (venta.getCreatedAt() != null) {
        String dia = venta.getCreatedAt()
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL,
                        new Locale("es", "PE"));

        // Capitalizar primera letra
        String diaCapitalizado = dia.substring(0, 1)
                .toUpperCase() + dia.substring(1);

        resultado.merge(diaCapitalizado,
                venta.getTotal() != null
                        ? venta.getTotal() : BigDecimal.ZERO,
                BigDecimal::add);
      }
    });

    return resultado;
  }

  private List<Dashboard.TopProducto> calcularTopProductos(
          List<Venta> ventas,List<Producto> productos, int limite) {

    // Agrupar items por producto
    Map<String, int[]> productoStats = new HashMap<>();
    // int[0] = unidades, int[1] = ingresos (en centimos para precision)

    ventas.stream()
            .filter(v -> v.getItems() != null)
            .flatMap(v -> v.getItems().stream())
            .forEach(item -> {
              String key = item.getProductId() + "|" +
                      item.getProductName();
              productoStats.merge(key,
                      new int[]{item.getQuantity(),
                              item.getSubtotal().intValue()},
                      (a, b) -> new int[]{
                              a[0] + b[0], a[1] + b[1]});
            });

    return productoStats.entrySet().stream()
            .map(entry -> {
              String[] parts = entry.getKey().split("\\|");
              String nombre = parts.length > 1
                      ? parts[1] : "Desconocido";
              String categoria = productos.stream()
                      .filter(p -> p.getName().equals(nombre))
                      .map(Producto::getCategoria)
                      .findFirst()
                      .orElse("");
              return new Dashboard.TopProducto(
                      nombre,
                      categoria,
                      entry.getValue()[0],
                      BigDecimal.valueOf(entry.getValue()[1]));
            })
            .sorted(Comparator.comparing(
                    Dashboard.TopProducto::ingresos).reversed())
            .limit(limite)
            .toList();
  }

  // Productos con stock bajo
  public List<Producto> obtenerProductosStockBajo() {
    return productoRepository.findByStockBajo();
  }

  // Actividad reciente — últimas N ventas
  public List<Venta> obtenerActividadReciente(int limite) {
    return ventaRepository.findByStatus("completed")
            .stream()
            .sorted(Comparator.comparing(
                    Venta::getCreatedAt).reversed())
            .limit(limite)
            .toList();
  }

  // Productos más rentables — ordenados por margen
  public List<ProductoRentable> obtenerProductosRentables(
          List<Venta> ventas, List<Producto> productos, int limite) {

    Map<UUID, BigDecimal> costos = productos.stream()
            .collect(Collectors.toMap(
                    Producto::getId,
                    Producto::getCost,
                    (a, b) -> a));

    Map<UUID, BigDecimal> precios = productos.stream()
            .collect(Collectors.toMap(
                    Producto::getId,
                    Producto::getPrice,
                    (a, b) -> a));

    // Agrupar ventas por producto
    Map<UUID, int[]> stats = new HashMap<>();

    ventas.stream()
            .filter(v -> v.getItems() != null)
            .flatMap(v -> v.getItems().stream())
            .forEach(item -> stats.merge(
                    item.getProductId(),
                    new int[]{
                            item.getQuantity(),
                            item.getSubtotal().intValue()
                    },
                    (a, b) -> new int[]{
                            a[0] + b[0],
                            a[1] + b[1]
                    }));

    return stats.entrySet().stream()
            .map(entry -> {
              UUID id = entry.getKey();
              BigDecimal costo = costos.getOrDefault(
                      id, BigDecimal.ZERO);
              BigDecimal precio = precios.getOrDefault(
                      id, BigDecimal.ZERO);

              // Margen = (precio - costo) / precio * 100
              BigDecimal margen = BigDecimal.ZERO;
              if (precio.compareTo(BigDecimal.ZERO) > 0) {
                margen = precio.subtract(costo)
                        .divide(precio, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(1, RoundingMode.HALF_UP);
              }

              String nombre = productos.stream()
                      .filter(p -> p.getId().equals(id))
                      .map(Producto::getName)
                      .findFirst()
                      .orElse("Desconocido");

              String categoria = productos.stream()
                      .filter(p -> p.getId().equals(id))
                      .map(Producto::getCategoria)
                      .findFirst()
                      .orElse("");

              return new ProductoRentable(
                      nombre,
                      categoria,
                      entry.getValue()[0],
                      BigDecimal.valueOf(entry.getValue()[1]),
                      margen,
                      BigDecimal.valueOf(entry.getValue()[1])
                              .multiply(margen)
                              .divide(BigDecimal.valueOf(100),
                                      2, RoundingMode.HALF_UP));
            })
            .sorted(Comparator.comparing(
                    ProductoRentable::margen).reversed())
            .limit(limite)
            .toList();
  }

  // Proyección próxima semana — promedio últimas 4 semanas
  public ProyeccionSemana calcularProyeccion(
          List<Venta> todasLasVentas) {

    LocalDateTime ahora = LocalDateTime.now();
    BigDecimal totalUltimas4 = BigDecimal.ZERO;
    int semanasConVentas = 0;

    for (int i = 1; i <= 4; i++) {
      LocalDateTime inicio = ahora
              .minusWeeks(i)
              .with(DayOfWeek.MONDAY)
              .withHour(0).withMinute(0).withSecond(0);
      LocalDateTime fin = inicio.plusWeeks(1)
              .minusSeconds(1);

      BigDecimal ingresosSemana = calcularIngresos(
              filtrarPorFecha(todasLasVentas, inicio, fin));

      if (ingresosSemana.compareTo(BigDecimal.ZERO) > 0) {
        totalUltimas4 = totalUltimas4.add(ingresosSemana);
        semanasConVentas++;
      }
    }

    BigDecimal proyeccion = semanasConVentas > 0
            ? totalUltimas4.divide(
            BigDecimal.valueOf(semanasConVentas),
            2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

    // Confianza basada en cuántas semanas tienen datos
    String confianza = semanasConVentas >= 3 ? "Alta"
            : semanasConVentas == 2 ? "Media" : "Baja";

    // Ventas estimadas = promedio de ventas por semana
    long ventasEstimadas = todasLasVentas.stream()
            .filter(v -> v.getCreatedAt() != null
                    && v.getCreatedAt().isAfter(
                    ahora.minusWeeks(4)))
            .count() / Math.max(semanasConVentas, 1);

    return new ProyeccionSemana(
            proyeccion, ventasEstimadas, confianza);
  }

  // Records para datos adicionales del dashboard
  public record ProductoRentable(
          String nombre,
          String categoria,
          Integer unidadesVendidas,
          BigDecimal ingresos,
          BigDecimal margen,
          BigDecimal gananciaGenerada) {}

  public record ProyeccionSemana(
          BigDecimal ingresosEstimados,
          Long ventasEstimadas,
          String confianza) {}
}
