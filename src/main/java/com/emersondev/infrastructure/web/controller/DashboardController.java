package com.emersondev.infrastructure.web.controller;

import com.emersondev.application.usecase.dashboard.ObtenerDashboardUseCase;
import com.emersondev.domain.model.Dashboard;
import com.emersondev.infrastructure.web.dto.response.DashboardResponse;
import com.emersondev.infrastructure.web.dto.response.DashboardResponse.TopProductoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

  private final ObtenerDashboardUseCase obtenerDashboardUseCase;

  // GET /api/dashboard
  @GetMapping
  public ResponseEntity<DashboardResponse> obtenerDashboard(
          @RequestParam(defaultValue = "semana") String periodo) {

    Dashboard dashboard = obtenerDashboardUseCase.ejecutar(periodo);
    return ResponseEntity.ok(toResponse(dashboard));
  }

  // GET /api/dashboard/hoy
  @GetMapping("/hoy")
  public ResponseEntity<DashboardResponse> obtenerDashboardHoy() {
    Dashboard dashboard = obtenerDashboardUseCase.ejecutar("hoy");
    return ResponseEntity.ok(toResponse(dashboard));
  }

  private DashboardResponse toResponse(Dashboard dashboard) {
    DashboardResponse response = new DashboardResponse();

    // Métricas base
    response.setVentasHoy(dashboard.getVentasHoy());
    response.setIngresosHoy(dashboard.getIngresosHoy());
    response.setIngresosSemana(dashboard.getIngresosSemana());
    response.setGananciaNeta(dashboard.getGananciaNeta());
    response.setInversionTotal(dashboard.getInversionTotal());
    response.setMejorDiaSemana(dashboard.getMejorDiaSemana());
    response.setIngresosMejorDia(dashboard.getIngresosMejorDia());
    response.setMargenRentabilidad(dashboard.getMargenRentabilidad());
    response.setRoi(dashboard.getRoi());
    response.setTicketPromedio(dashboard.getTicketPromedio());
    response.setIngresosSemanaAnterior(
            dashboard.getIngresosSemanaAnterior());
    response.setCrecimientoSemanal(dashboard.getCrecimientoSemanal());
    response.setVentasPorDia(dashboard.getVentasPorDia());
    response.setConversion(dashboard.getConversion());

    // Top productos
    if (dashboard.getTopProductos() != null) {
      response.setTopProductos(dashboard.getTopProductos()
              .stream().map(tp -> {
                var tpr = new DashboardResponse
                        .TopProductoResponse();
                tpr.setNombre(tp.nombre());
                tpr.setCategoria(tp.categoria());
                tpr.setUnidadesVendidas(tp.unidadesVendidas());
                tpr.setIngresos(tp.ingresos());
                return tpr;
              }).toList());
    }

    // Actividad reciente
    if (dashboard.getActividadReciente() != null) {
      response.setActividadReciente(
              dashboard.getActividadReciente().stream()
                      .map(venta -> {
                        var ar = new DashboardResponse
                                .ActividadRecienteResponse();
                        ar.setSaleNumber(venta.getSaleNumber());
                        ar.setPaymentMethod(
                                venta.getPaymentMethod());
                        ar.setTotal(venta.getTotal());
                        ar.setStatus(venta.getStatus());
                        ar.setCreatedAt(venta.getCreatedAt()
                                .toString());
                        // Producto principal = primer item
                        if (venta.getItems() != null
                                && !venta.getItems().isEmpty()) {
                          ar.setProductoPrincipal(
                                  venta.getItems()
                                          .get(0)
                                          .getProductName());
                        }
                        return ar;
                      }).toList());
    }

    // Productos rentables
    if (dashboard.getProductosRentables() != null) {
      response.setProductosRentables(
              dashboard.getProductosRentables().stream()
                      .map(pr -> {
                        var prr = new DashboardResponse
                                .ProductoRentableResponse();
                        prr.setNombre(pr.nombre());
                        prr.setCategoria(pr.categoria());
                        prr.setUnidadesVendidas(
                                pr.unidadesVendidas());
                        prr.setIngresos(pr.ingresos());
                        prr.setMargen(pr.margen());
                        prr.setGananciaGenerada(
                                pr.gananciaGenerada());
                        return prr;
                      }).toList());
    }

    // Stock bajo
    if (dashboard.getProductosStockBajo() != null) {
      response.setProductosStockBajo(
              dashboard.getProductosStockBajo().stream()
                      .map(p -> {
                        var sb = new DashboardResponse
                                .StockBajoResponse();
                        sb.setNombre(p.getName());
                        sb.setCategoria(p.getCategoria());
                        sb.setStock(p.getStock());
                        sb.setMinStock(p.getMinStock());
                        return sb;
                      }).toList());
    }

    // Proyección
    if (dashboard.getProyeccion() != null) {
      var proy = dashboard.getProyeccion();
      var proyR = new DashboardResponse.ProyeccionResponse();
      proyR.setIngresosEstimados(proy.ingresosEstimados());
      proyR.setVentasEstimadas(proy.ventasEstimadas());
      proyR.setConfianza(proy.confianza());
      response.setProyeccion(proyR);
    }

    return response;
  }
}
