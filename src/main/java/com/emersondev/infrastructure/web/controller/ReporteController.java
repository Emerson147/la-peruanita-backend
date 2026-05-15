package com.emersondev.infrastructure.web.controller;

import com.emersondev.application.usecase.reportes.ExportarReporteUseCase;
import com.emersondev.application.usecase.reportes.ObtenerReporteUseCase;
import com.emersondev.domain.model.ReporteVentas;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

  private final ObtenerReporteUseCase obtenerReporteUseCase;
  private final ExportarReporteUseCase exportarReporteUseCase;

  // GET /api/reportes?periodo=semana|mes|anio
  @GetMapping
  public ResponseEntity<ReporteVentas> obtenerReporte(
          @RequestParam(defaultValue = "semana") String periodo,
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime desde,
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime hasta) {

    LocalDateTime[] rango = calcularRango(
            periodo, desde, hasta);

    ReporteVentas reporte = obtenerReporteUseCase
            .ejecutar(rango[0], rango[1], periodo);

    return ResponseEntity.ok(reporte);
  }

  // GET /api/reportes/exportar/excel
  @GetMapping("/exportar/excel")
  public ResponseEntity<byte[]> exportarExcel(
          @RequestParam(defaultValue = "semana")
          String periodo) throws Exception {

    LocalDateTime[] rango = calcularRango(
            periodo, null, null);
    ReporteVentas reporte = obtenerReporteUseCase
            .ejecutar(rango[0], rango[1], periodo);

    byte[] excel = exportarReporteUseCase
            .exportarExcel(reporte);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=reporte-denraf-"
                            + periodo + ".xlsx")
            .contentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-" +
                            "officedocument.spreadsheetml.sheet"))
            .body(excel);
  }

  // GET /api/reportes/exportar/pdf
  @GetMapping("/exportar/pdf")
  public ResponseEntity<byte[]> exportarPDF(
          @RequestParam(defaultValue = "semana")
          String periodo) throws Exception {

    LocalDateTime[] rango = calcularRango(
            periodo, null, null);
    ReporteVentas reporte = obtenerReporteUseCase
            .ejecutar(rango[0], rango[1], periodo);

    byte[] pdf = exportarReporteUseCase
            .exportarPDF(reporte);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=reporte-denraf-"
                            + periodo + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
  }

  // GET /api/reportes/exportar/csv
  @GetMapping("/exportar/csv")
  public ResponseEntity<byte[]> exportarCSV(
          @RequestParam(defaultValue = "semana")
          String periodo) throws Exception {

    LocalDateTime[] rango = calcularRango(
            periodo, null, null);
    ReporteVentas reporte = obtenerReporteUseCase
            .ejecutar(rango[0], rango[1], periodo);

    byte[] csv = exportarReporteUseCase
            .exportarCSV(reporte);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=reporte-denraf-"
                            + periodo + ".csv")
            .contentType(MediaType.parseMediaType(
                    "text/csv; charset=UTF-8"))
            .body(csv);
  }

  // Helper — calcular rango de fechas
  private LocalDateTime[] calcularRango(
          String periodo,
          LocalDateTime desde,
          LocalDateTime hasta) {

    if (desde != null && hasta != null) {
      return new LocalDateTime[]{desde, hasta};
    }

    LocalDateTime ahora = LocalDateTime.now();

    return switch (periodo) {
      case "hoy" -> new LocalDateTime[]{
              ahora.withHour(0).withMinute(0)
                      .withSecond(0),
              ahora};
      case "mes" -> new LocalDateTime[]{
              ahora.withDayOfMonth(1).withHour(0)
                      .withMinute(0).withSecond(0),
              ahora};
      case "anio" -> new LocalDateTime[]{
              ahora.withDayOfYear(1).withHour(0)
                      .withMinute(0).withSecond(0),
              ahora};
      default -> new LocalDateTime[]{ // semana
              ahora.minusWeeks(1),
              ahora};
    };
  }
}