package com.emersondev.application.usecase.reportes;

import com.emersondev.domain.model.ReporteVentas;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportarReporteUseCase {

  private static final DateTimeFormatter FORMATTER =
          DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  // =============================================
  // EXPORTAR A EXCEL
  // =============================================

  public byte[] exportarExcel(ReporteVentas reporte)
          throws IOException {

    try (Workbook wb = new XSSFWorkbook();
         ByteArrayOutputStream out =
                 new ByteArrayOutputStream()) {

      // Estilos
      CellStyle headerStyle = wb.createCellStyle();
      Font headerFont = wb.createFont();
      headerFont.setBold(true);
      headerFont.setFontHeightInPoints((short) 12);
      headerStyle.setFont(headerFont);
      headerStyle.setFillForegroundColor(
              IndexedColors.DARK_BLUE.getIndex());
      headerStyle.setFillPattern(
              FillPatternType.SOLID_FOREGROUND);
      Font whiteFont = wb.createFont();
      whiteFont.setColor(IndexedColors.WHITE.getIndex());
      whiteFont.setBold(true);
      headerStyle.setFont(whiteFont);

      CellStyle numberStyle = wb.createCellStyle();
      DataFormat format = wb.createDataFormat();
      numberStyle.setDataFormat(
              format.getFormat("#,##0.00"));

      // Hoja 1 — Resumen
      crearHojaResumen(wb, reporte,
              headerStyle, numberStyle);

      // Hoja 2 — Top Productos
      crearHojaProductos(wb, reporte,
              headerStyle, numberStyle);

      // Hoja 3 — Análisis ABC
      crearHojaABC(wb, reporte,
              headerStyle, numberStyle);

      // Hoja 4 — Vendedores
      crearHojaVendedores(wb, reporte,
              headerStyle, numberStyle);

      wb.write(out);
      return out.toByteArray();
    }
  }

  private void crearHojaResumen(Workbook wb,
                                ReporteVentas reporte,
                                CellStyle headerStyle,
                                CellStyle numberStyle) {

    Sheet sheet = wb.createSheet("Resumen");
    sheet.setColumnWidth(0, 8000);
    sheet.setColumnWidth(1, 5000);

    int row = 0;

    // Título
    Row titulo = sheet.createRow(row++);
    Cell tituloCell = titulo.createCell(0);
    tituloCell.setCellValue("REPORTE DENRAF — " +
            reporte.getPeriodo().toUpperCase());
    tituloCell.setCellStyle(headerStyle);

    row++; // espacio

    // Datos del resumen
    crearFila(sheet, row++,
            "Período",
            reporte.getDesde().format(FORMATTER) + " → " +
                    reporte.getHasta().format(FORMATTER));

    crearFilaNumero(sheet, row++, numberStyle,
            "Ingresos Totales (S/)",
            reporte.getIngresosTotales());

    crearFilaNumero(sheet, row++, numberStyle,
            "Ganancia Neta (S/)",
            reporte.getGananciaNeta());

    crearFila(sheet, row++,
            "Total Ventas",
            String.valueOf(reporte.getTotalVentas()));

    crearFila(sheet, row++,
            "Productos Vendidos",
            String.valueOf(reporte.getProductosVendidos()));

    crearFilaNumero(sheet, row++, numberStyle,
            "Ticket Promedio (S/)",
            reporte.getTicketPromedio());

    crearFilaNumero(sheet, row++, numberStyle,
            "Ventas Ferias (S/)",
            reporte.getVentasFerias());

    crearFilaNumero(sheet, row++, numberStyle,
            "Ventas Tienda (S/)",
            reporte.getVentasTienda());

    crearFila(sheet, row++,
            "Mejor Feria",
            reporte.getMejorFeria() != null
                    ? reporte.getMejorFeria() : "-");
  }

  private void crearHojaProductos(Workbook wb,
                                  ReporteVentas reporte,
                                  CellStyle headerStyle,
                                  CellStyle numberStyle) {

    Sheet sheet = wb.createSheet("Top Productos");
    sheet.setColumnWidth(0, 8000);
    sheet.setColumnWidth(1, 5000);
    sheet.setColumnWidth(2, 4000);
    sheet.setColumnWidth(3, 4000);
    sheet.setColumnWidth(4, 4000);

    Row header = sheet.createRow(0);
    String[] cols = {"Producto", "Categoría",
            "Unidades", "Ingresos (S/)", "Margen %"};
    for (int i = 0; i < cols.length; i++) {
      Cell cell = header.createCell(i);
      cell.setCellValue(cols[i]);
      cell.setCellStyle(headerStyle);
    }

    int row = 1;
    if (reporte.getTopProductos() != null) {
      for (var p : reporte.getTopProductos()) {
        Row r = sheet.createRow(row++);
        r.createCell(0).setCellValue(p.nombre());
        r.createCell(1).setCellValue(p.categoria());
        r.createCell(2).setCellValue(
                p.unidadesVendidas());
        Cell ingCell = r.createCell(3);
        ingCell.setCellValue(
                p.ingresos().doubleValue());
        ingCell.setCellStyle(numberStyle);
        r.createCell(4).setCellValue(
                p.margen().doubleValue());
      }
    }
  }

  private void crearHojaABC(Workbook wb,
                            ReporteVentas reporte,
                            CellStyle headerStyle,
                            CellStyle numberStyle) {

    Sheet sheet = wb.createSheet("Análisis ABC");
    sheet.setColumnWidth(0, 3000);
    sheet.setColumnWidth(1, 8000);
    sheet.setColumnWidth(2, 5000);
    sheet.setColumnWidth(3, 4000);
    sheet.setColumnWidth(4, 4000);
    sheet.setColumnWidth(5, 4000);

    Row header = sheet.createRow(0);
    String[] cols = {"Clase", "Producto",
            "Categoría", "Unidades",
            "Ingresos (S/)", "% del Total"};
    for (int i = 0; i < cols.length; i++) {
      Cell cell = header.createCell(i);
      cell.setCellValue(cols[i]);
      cell.setCellStyle(headerStyle);
    }

    int row = 1;
    List<ReporteVentas.ProductoABC> todos =
            new java.util.ArrayList<>();
    if (reporte.getProductosA() != null)
      todos.addAll(reporte.getProductosA());
    if (reporte.getProductosB() != null)
      todos.addAll(reporte.getProductosB());
    if (reporte.getProductosC() != null)
      todos.addAll(reporte.getProductosC());

    for (var p : todos) {
      Row r = sheet.createRow(row++);
      r.createCell(0).setCellValue(p.clasificacion());
      r.createCell(1).setCellValue(p.nombre());
      r.createCell(2).setCellValue(p.categoria());
      r.createCell(3).setCellValue(p.unidades());
      Cell ingCell = r.createCell(4);
      ingCell.setCellValue(p.ingresos().doubleValue());
      ingCell.setCellStyle(numberStyle);
      r.createCell(5).setCellValue(
              p.porcentajeDelTotal());
    }
  }

  private void crearHojaVendedores(Workbook wb,
                                   ReporteVentas reporte,
                                   CellStyle headerStyle,
                                   CellStyle numberStyle) {

    Sheet sheet = wb.createSheet("Vendedores");
    sheet.setColumnWidth(0, 6000);
    sheet.setColumnWidth(1, 4000);
    sheet.setColumnWidth(2, 4000);
    sheet.setColumnWidth(3, 4000);
    sheet.setColumnWidth(4, 4000);

    Row header = sheet.createRow(0);
    String[] cols = {"Vendedor", "Ventas",
            "Ingresos (S/)", "Ticket Prom. (S/)",
            "Participación %"};
    for (int i = 0; i < cols.length; i++) {
      Cell cell = header.createCell(i);
      cell.setCellValue(cols[i]);
      cell.setCellStyle(headerStyle);
    }

    int row = 1;
    if (reporte.getVentasPorVendedor() != null) {
      for (var v : reporte.getVentasPorVendedor()) {
        Row r = sheet.createRow(row++);
        r.createCell(0).setCellValue(v.nombre());
        r.createCell(1).setCellValue(v.totalVentas());
        Cell ingCell = r.createCell(2);
        ingCell.setCellValue(
                v.totalIngresos().doubleValue());
        ingCell.setCellStyle(numberStyle);
        Cell ticketCell = r.createCell(3);
        ticketCell.setCellValue(
                v.ticketPromedio().doubleValue());
        ticketCell.setCellStyle(numberStyle);
        r.createCell(4).setCellValue(
                v.participacion());
      }
    }
  }

  // =============================================
  // EXPORTAR A PDF
  // =============================================

  public byte[] exportarPDF(ReporteVentas reporte)
          throws DocumentException, IOException {

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document doc = new Document(PageSize.A4);
    PdfWriter.getInstance(doc, out);
    doc.open();

    // Fuentes
    com.lowagie.text.Font titleFont = FontFactory.getFont(
            FontFactory.HELVETICA_BOLD, 18,
            new java.awt.Color(0, 51, 102));
    com.lowagie.text.Font sectionFont = FontFactory.getFont(
            FontFactory.HELVETICA_BOLD, 13,
            new java.awt.Color(0, 51, 102));
    com.lowagie.text.Font normalFont = FontFactory.getFont(
            FontFactory.HELVETICA, 10);
    com.lowagie.text.Font boldFont = FontFactory.getFont(
            FontFactory.HELVETICA_BOLD, 10);

    // Título
    Paragraph titulo = new Paragraph(
            "REPORTE DENRAF", titleFont);
    titulo.setAlignment(Element.ALIGN_CENTER);
    doc.add(titulo);

    Paragraph subtitulo = new Paragraph(
            reporte.getPeriodo().toUpperCase() +
                    " | " + reporte.getDesde().format(FORMATTER) +
                    " → " + reporte.getHasta().format(FORMATTER),
            normalFont);
    subtitulo.setAlignment(Element.ALIGN_CENTER);
    subtitulo.setSpacingAfter(20);
    doc.add(subtitulo);

    // Resumen ejecutivo
    doc.add(new Paragraph("RESUMEN EJECUTIVO",
            sectionFont));
    doc.add(new LineSeparator());

    PdfPTable resumenTable = new PdfPTable(2);
    resumenTable.setWidthPercentage(100);
    resumenTable.setSpacingBefore(10);
    resumenTable.setSpacingAfter(20);

    agregarFilaPDF(resumenTable, "Ingresos Totales",
            "S/ " + reporte.getIngresosTotales(),
            boldFont, normalFont);
    agregarFilaPDF(resumenTable, "Ganancia Neta",
            "S/ " + reporte.getGananciaNeta(),
            boldFont, normalFont);
    agregarFilaPDF(resumenTable, "Total Ventas",
            String.valueOf(reporte.getTotalVentas()),
            boldFont, normalFont);
    agregarFilaPDF(resumenTable, "Productos Vendidos",
            String.valueOf(reporte.getProductosVendidos()),
            boldFont, normalFont);
    agregarFilaPDF(resumenTable, "Ticket Promedio",
            "S/ " + reporte.getTicketPromedio(),
            boldFont, normalFont);
    agregarFilaPDF(resumenTable, "Ventas Ferias",
            "S/ " + reporte.getVentasFerias(),
            boldFont, normalFont);
    agregarFilaPDF(resumenTable, "Ventas Tienda",
            "S/ " + reporte.getVentasTienda(),
            boldFont, normalFont);

    doc.add(resumenTable);

    // Top Productos
    doc.add(new Paragraph("TOP PRODUCTOS", sectionFont));
    doc.add(new LineSeparator());

    if (reporte.getTopProductos() != null
            && !reporte.getTopProductos().isEmpty()) {

      PdfPTable prodTable = new PdfPTable(4);
      prodTable.setWidthPercentage(100);
      prodTable.setSpacingBefore(10);
      prodTable.setSpacingAfter(20);
      prodTable.setWidths(new float[]{3f, 1.5f, 1.5f, 1.5f});

      // Header
      java.awt.Color headerColor = new java.awt.Color(0, 51, 102);
      agregarHeaderPDF(prodTable,
              "Producto", headerColor);
      agregarHeaderPDF(prodTable,
              "Unidades", headerColor);
      agregarHeaderPDF(prodTable,
              "Ingresos", headerColor);
      agregarHeaderPDF(prodTable,
              "Margen %", headerColor);

      reporte.getTopProductos().stream()
              .limit(10)
              .forEach(p -> {
                prodTable.addCell(new Phrase(
                        p.nombre(), normalFont));
                prodTable.addCell(new Phrase(
                        String.valueOf(
                                p.unidadesVendidas()),
                        normalFont));
                prodTable.addCell(new Phrase(
                        "S/ " + p.ingresos(),
                        normalFont));
                prodTable.addCell(new Phrase(
                        p.margen() + "%", normalFont));
              });

      doc.add(prodTable);
    }

    // Análisis ABC
    doc.add(new Paragraph("ANÁLISIS ABC", sectionFont));
    doc.add(new LineSeparator());

    PdfPTable abcTable = new PdfPTable(3);
    abcTable.setWidthPercentage(100);
    abcTable.setSpacingBefore(10);

    java.awt.Color colorA = new java.awt.Color(39, 174, 96);
    java.awt.Color colorB = new java.awt.Color(243, 156, 18);
    java.awt.Color colorC = new java.awt.Color(231, 76, 60);

    int countA = reporte.getProductosA() != null
            ? reporte.getProductosA().size() : 0;
    int countB = reporte.getProductosB() != null
            ? reporte.getProductosB().size() : 0;
    int countC = reporte.getProductosC() != null
            ? reporte.getProductosC().size() : 0;

    agregarCeldaColorPDF(abcTable,
            "A — Productos Estrella\n" + countA +
                    " productos (80% ingresos)", colorA);
    agregarCeldaColorPDF(abcTable,
            "B — Productos Regulares\n" + countB +
                    " productos (15% ingresos)", colorB);
    agregarCeldaColorPDF(abcTable,
            "C — Productos Lentos\n" + countC +
                    " productos (5% ingresos)", colorC);

    doc.add(abcTable);

    doc.close();
    return out.toByteArray();
  }

  // =============================================
  // EXPORTAR A CSV
  // =============================================

  public byte[] exportarCSV(ReporteVentas reporte)
          throws IOException {

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    OutputStreamWriter writer = new OutputStreamWriter(
            out, java.nio.charset.StandardCharsets.UTF_8);

    try (CSVWriter csv = new CSVWriter(writer)) {

      // BOM para Excel en español
      out.write(0xEF);
      out.write(0xBB);
      out.write(0xBF);

      // RESUMEN
      csv.writeNext(new String[]{"=== RESUMEN ==="});
      csv.writeNext(new String[]{
              "Período", reporte.getPeriodo()});
      csv.writeNext(new String[]{
              "Desde",
              reporte.getDesde().format(FORMATTER)});
      csv.writeNext(new String[]{
              "Hasta",
              reporte.getHasta().format(FORMATTER)});
      csv.writeNext(new String[]{
              "Ingresos Totales",
              String.valueOf(reporte.getIngresosTotales())});
      csv.writeNext(new String[]{
              "Ganancia Neta",
              String.valueOf(reporte.getGananciaNeta())});
      csv.writeNext(new String[]{
              "Total Ventas",
              String.valueOf(reporte.getTotalVentas())});
      csv.writeNext(new String[]{
              "Ticket Promedio",
              String.valueOf(reporte.getTicketPromedio())});
      csv.writeNext(new String[]{""});

      // TOP PRODUCTOS
      csv.writeNext(new String[]{
              "=== TOP PRODUCTOS ==="});
      csv.writeNext(new String[]{
              "Producto", "Categoría",
              "Unidades", "Ingresos", "Margen %"});

      if (reporte.getTopProductos() != null) {
        reporte.getTopProductos().forEach(p ->
                csv.writeNext(new String[]{
                        p.nombre(),
                        p.categoria(),
                        String.valueOf(p.unidadesVendidas()),
                        String.valueOf(p.ingresos()),
                        String.valueOf(p.margen())}));
      }

      csv.writeNext(new String[]{""});

      // ANÁLISIS ABC
      csv.writeNext(new String[]{
              "=== ANÁLISIS ABC ==="});
      csv.writeNext(new String[]{
              "Clase", "Producto", "Categoría",
              "Unidades", "Ingresos", "% del Total"});

      escribirProductosABC(csv,
              reporte.getProductosA());
      escribirProductosABC(csv,
              reporte.getProductosB());
      escribirProductosABC(csv,
              reporte.getProductosC());

      csv.writeNext(new String[]{""});

      // VENDEDORES
      csv.writeNext(new String[]{
              "=== VENDEDORES ==="});
      csv.writeNext(new String[]{
              "Vendedor", "Ventas",
              "Ingresos", "Ticket Prom.", "Participación %"});

      if (reporte.getVentasPorVendedor() != null) {
        reporte.getVentasPorVendedor().forEach(v ->
                csv.writeNext(new String[]{
                        v.nombre(),
                        String.valueOf(v.totalVentas()),
                        String.valueOf(v.totalIngresos()),
                        String.valueOf(v.ticketPromedio()),
                        String.valueOf(v.participacion())}));
      }
    }

    return out.toByteArray();
  }

  // =============================================
  // HELPERS
  // =============================================

  private void crearFila(Sheet sheet, int rowNum,
                         String label, String value) {
    Row row = sheet.createRow(rowNum);
    row.createCell(0).setCellValue(label);
    row.createCell(1).setCellValue(value);
  }

  private void crearFilaNumero(Sheet sheet, int rowNum,
                               CellStyle style, String label, BigDecimal value) {
    Row row = sheet.createRow(rowNum);
    row.createCell(0).setCellValue(label);
    Cell cell = row.createCell(1);
    cell.setCellValue(value != null
            ? value.doubleValue() : 0);
    cell.setCellStyle(style);
  }

  private void agregarFilaPDF(PdfPTable table,
                              String label, String value,
                              com.lowagie.text.Font boldFont, com.lowagie.text.Font normalFont) {
    table.addCell(new Phrase(label, boldFont));
    table.addCell(new Phrase(value, normalFont));
  }

  private void agregarHeaderPDF(PdfPTable table,
                                String text, java.awt.Color color) {
    PdfPCell cell = new PdfPCell(
            new Phrase(text, FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD, 10,
                    java.awt.Color.WHITE)));
    cell.setBackgroundColor(color);
    cell.setPadding(5);
    table.addCell(cell);
  }

  private void agregarCeldaColorPDF(PdfPTable table,
                                    String text, java.awt.Color color) {
    PdfPCell cell = new PdfPCell(
            new Phrase(text, FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD, 10,
                    java.awt.Color.WHITE)));
    cell.setBackgroundColor(color);
    cell.setPadding(10);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(cell);
  }

  private void escribirProductosABC(CSVWriter csv,
                                    List<ReporteVentas.ProductoABC> productos) {
    if (productos == null) return;
    productos.forEach(p -> csv.writeNext(new String[]{
            p.clasificacion(),
            p.nombre(),
            p.categoria(),
            String.valueOf(p.unidades()),
            String.valueOf(p.ingresos()),
            String.valueOf(p.porcentajeDelTotal())}));
  }
}

