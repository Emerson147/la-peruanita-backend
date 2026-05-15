package com.emersondev.infrastructure.web.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardResponse {

  private Integer ventasHoy;
  private BigDecimal ingresosHoy;
  private BigDecimal ingresosSemana;
  private BigDecimal gananciaNeta;
  private BigDecimal inversionTotal;
  private String mejorDiaSemana;
  private BigDecimal ingresosMejorDia;
  private BigDecimal margenRentabilidad;
  private BigDecimal roi;
  private BigDecimal ticketPromedio;
  private BigDecimal ingresosSemanaAnterior;
  private BigDecimal crecimientoSemanal;
  private List<TopProductoResponse> topProductos;
  private Map<String, BigDecimal> ventasPorDia;
  private List<ActividadRecienteResponse> actividadReciente;
  private List<ProductoRentableResponse> productosRentables;
  private List<StockBajoResponse> productosStockBajo;
  private ProyeccionResponse proyeccion;
  private BigDecimal conversion;

  public DashboardResponse() {}

  // Clase interna
  public static class TopProductoResponse {
    private String nombre;
    private String categoria;
    private Integer unidadesVendidas;
    private BigDecimal ingresos;

    public TopProductoResponse() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getUnidadesVendidas() { return unidadesVendidas; }
    public void setUnidadesVendidas(Integer u) { this.unidadesVendidas = u; }

    public BigDecimal getIngresos() { return ingresos; }
    public void setIngresos(BigDecimal ingresos) { this.ingresos = ingresos; }
  }

  public static class ActividadRecienteResponse {
    private String saleNumber;
    private String productoPrincipal;
    private String paymentMethod;
    private BigDecimal total;
    private String createdAt;
    private String status;

    public ActividadRecienteResponse() {}

    public String getSaleNumber() { return saleNumber; }
    public void setSaleNumber(String s) { this.saleNumber = s; }

    public String getProductoPrincipal() { return productoPrincipal; }
    public void setProductoPrincipal(String p) {
      this.productoPrincipal = p; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String p) { this.paymentMethod = p; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal t) { this.total = t; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String c) { this.createdAt = c; }

    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
  }

  // Clase interna — producto rentable
  public static class ProductoRentableResponse {
    private String nombre;
    private String categoria;
    private Integer unidadesVendidas;
    private BigDecimal ingresos;
    private BigDecimal margen;
    private BigDecimal gananciaGenerada;

    public ProductoRentableResponse() {}

    public String getNombre() { return nombre; }
    public void setNombre(String n) { this.nombre = n; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String c) { this.categoria = c; }

    public Integer getUnidadesVendidas() { return unidadesVendidas; }
    public void setUnidadesVendidas(Integer u) {
      this.unidadesVendidas = u; }

    public BigDecimal getIngresos() { return ingresos; }
    public void setIngresos(BigDecimal i) { this.ingresos = i; }

    public BigDecimal getMargen() { return margen; }
    public void setMargen(BigDecimal m) { this.margen = m; }

    public BigDecimal getGananciaGenerada() { return gananciaGenerada; }
    public void setGananciaGenerada(BigDecimal g) {
      this.gananciaGenerada = g; }
  }

  // Clase interna — stock bajo
  public static class StockBajoResponse {
    private String nombre;
    private String categoria;
    private Integer stock;
    private Integer minStock;

    public StockBajoResponse() {}

    public String getNombre() { return nombre; }
    public void setNombre(String n) { this.nombre = n; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String c) { this.categoria = c; }

    public Integer getStock() { return stock; }
    public void setStock(Integer s) { this.stock = s; }

    public Integer getMinStock() { return minStock; }
    public void setMinStock(Integer m) { this.minStock = m; }
  }

  // Clase interna — proyección
  public static class ProyeccionResponse {
    private BigDecimal ingresosEstimados;
    private Long ventasEstimadas;
    private String confianza;

    public ProyeccionResponse() {}

    public BigDecimal getIngresosEstimados() { return ingresosEstimados; }
    public void setIngresosEstimados(BigDecimal i) {
      this.ingresosEstimados = i; }

    public Long getVentasEstimadas() { return ventasEstimadas; }
    public void setVentasEstimadas(Long v) { this.ventasEstimadas = v; }

    public String getConfianza() { return confianza; }
    public void setConfianza(String c) { this.confianza = c; }
  }

  // Getters y setters de los campos nuevos
  public List<ActividadRecienteResponse> getActividadReciente() {
    return actividadReciente; }
  public void setActividadReciente(
          List<ActividadRecienteResponse> a) {
    this.actividadReciente = a; }

  public List<ProductoRentableResponse> getProductosRentables() {
    return productosRentables; }
  public void setProductosRentables(
          List<ProductoRentableResponse> p) {
    this.productosRentables = p; }

  public List<StockBajoResponse> getProductosStockBajo() {
    return productosStockBajo; }
  public void setProductosStockBajo(
          List<StockBajoResponse> p) {
    this.productosStockBajo = p; }

  public ProyeccionResponse getProyeccion() { return proyeccion; }
  public void setProyeccion(ProyeccionResponse p) {
    this.proyeccion = p; }

  public BigDecimal getConversion() { return conversion; }
  public void setConversion(BigDecimal c) { this.conversion = c; }

  public Integer getVentasHoy() { return ventasHoy; }
  public void setVentasHoy(Integer v) { this.ventasHoy = v; }

  public BigDecimal getIngresosHoy() { return ingresosHoy; }
  public void setIngresosHoy(BigDecimal i) { this.ingresosHoy = i; }

  public BigDecimal getIngresosSemana() { return ingresosSemana; }
  public void setIngresosSemana(BigDecimal i) { this.ingresosSemana = i; }

  public BigDecimal getGananciaNeta() { return gananciaNeta; }
  public void setGananciaNeta(BigDecimal g) { this.gananciaNeta = g; }

  public BigDecimal getInversionTotal() { return inversionTotal; }
  public void setInversionTotal(BigDecimal i) { this.inversionTotal = i; }

  public String getMejorDiaSemana() { return mejorDiaSemana; }
  public void setMejorDiaSemana(String m) { this.mejorDiaSemana = m; }

  public BigDecimal getIngresosMejorDia() { return ingresosMejorDia; }
  public void setIngresosMejorDia(BigDecimal i) { this.ingresosMejorDia = i; }

  public BigDecimal getMargenRentabilidad() { return margenRentabilidad; }
  public void setMargenRentabilidad(BigDecimal m) { this.margenRentabilidad = m; }

  public BigDecimal getRoi() { return roi; }
  public void setRoi(BigDecimal roi) { this.roi = roi; }

  public BigDecimal getTicketPromedio() { return ticketPromedio; }
  public void setTicketPromedio(BigDecimal t) { this.ticketPromedio = t; }

  public BigDecimal getIngresosSemanaAnterior() { return ingresosSemanaAnterior; }
  public void setIngresosSemanaAnterior(BigDecimal i) {
    this.ingresosSemanaAnterior = i; }

  public BigDecimal getCrecimientoSemanal() { return crecimientoSemanal; }
  public void setCrecimientoSemanal(BigDecimal c) { this.crecimientoSemanal = c; }

  public List<TopProductoResponse> getTopProductos() { return topProductos; }
  public void setTopProductos(List<TopProductoResponse> t) {
    this.topProductos = t; }

  public Map<String, BigDecimal> getVentasPorDia() { return ventasPorDia; }
  public void setVentasPorDia(Map<String, BigDecimal> v) {
    this.ventasPorDia = v; }
}
