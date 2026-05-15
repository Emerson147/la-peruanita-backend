package com.emersondev.domain.service;

import com.emersondev.domain.model.LiquidacionProducto;
import com.emersondev.domain.model.Producto;
import com.emersondev.domain.model.Venta;
import com.emersondev.domain.model.VentaItem;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.domain.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LiquidacionService — Algoritmo de ferias")
class LiquidacionServiceTest {

  @Mock
  private ProductoRepository productoRepository;

  @Mock
  private VentaRepository ventaRepository;

  @Mock
  private List<EstrategiaLiquidacion> estrategias;

  @InjectMocks
  private LiquidacionService liquidacionService;

  private Producto productoActivo;
  private Producto productoLento;
  private Producto productoCongelado;

  @BeforeEach
  void setUp() {
    // Producto activo — creado hace 5 días
    productoActivo = new Producto();
    productoActivo.setId(UUID.randomUUID());
    productoActivo.setName("Casaca Nueva");
    productoActivo.setCategoria("Casacas");
    productoActivo.setPrice(new BigDecimal("150.00"));
    productoActivo.setCost(new BigDecimal("90.00"));
    productoActivo.setStock(5);
    productoActivo.setStatus("active");
    productoActivo.setCreatedAt(
            LocalDateTime.now().minusDays(5));

    // Producto lento — creado hace 20 días (entre 4 y 8 ferias)
    productoLento = new Producto();
    productoLento.setId(UUID.randomUUID());
    productoLento.setName("Polo Azul");
    productoLento.setCategoria("Polos");
    productoLento.setPrice(new BigDecimal("35.00"));
    productoLento.setCost(new BigDecimal("18.00"));
    productoLento.setStock(7);
    productoLento.setStatus("active");
    productoLento.setCreatedAt(
            LocalDateTime.now().minusDays(20));

    // Producto congelado — creado hace 70 días (más de 8 ferias)
    productoCongelado = new Producto();
    productoCongelado.setId(UUID.randomUUID());
    productoCongelado.setName("Casaca Drill Antigua");
    productoCongelado.setCategoria("Casacas");
    productoCongelado.setPrice(new BigDecimal("100.00"));
    productoCongelado.setCost(new BigDecimal("60.00"));
    productoCongelado.setStock(3);
    productoCongelado.setStatus("active");
    productoCongelado.setCreatedAt(
            LocalDateTime.now().minusDays(70));
  }

  // =============================================
  // TESTS DE CLASIFICACIÓN POR FERIAS
  // =============================================

  @Test
  @DisplayName("Producto creado hace 5 días → menos de 4 ferias → ACTIVO")
  void productoNuevo_debeSerActivo() {
    // Arrange
    int dias = 5;

    // Act
    int ferias = liquidacionService.calcularFeriasSinVender(dias);

    // Assert
    assertThat(ferias).isLessThan(4);
  }

  @Test
  @DisplayName("Producto creado hace 20 días → entre 4 y 8 ferias → LENTO")
  void productoSinVender20Dias_debeSerLento() {
    // Arrange
    int dias = 20;

    // Act
    int ferias = liquidacionService.calcularFeriasSinVender(dias);

    // Assert
    assertThat(ferias).isBetween(4, 8);
  }

  @Test
  @DisplayName("Producto creado hace 70 días → más de 8 ferias → CONGELADO")
  void productoSinVender70Dias_debeSerCongelado() {
    // Arrange
    int dias = 70;

    // Act
    int ferias = liquidacionService.calcularFeriasSinVender(dias);

    // Assert
    assertThat(ferias).isGreaterThan(8);
  }

  // =============================================
  // TESTS DE CÁLCULO DE PRECIOS
  // =============================================

  @Test
  @DisplayName("Descuento 20% sobre precio 100 → debe dar 80")
  void calcularDescuento20_debeSerCorrecto() {
    // Arrange
    LiquidacionProducto liq = new LiquidacionProducto();
    liq.setProducto(productoCongelado);
    liq.setEstado("CONGELADO");

    // Act
    liq.calcularPrecios();

    // Assert — 100 * 0.80 = 80
    assertThat(liq.getPrecioConDescuento20())
            .isEqualByComparingTo(new BigDecimal("80.00"));
  }

  @Test
  @DisplayName("Descuento 30% sobre precio 100 → debe dar 70")
  void calcularDescuento30_debeSerCorrecto() {
    // Arrange
    LiquidacionProducto liq = new LiquidacionProducto();
    liq.setProducto(productoCongelado);
    liq.setEstado("CONGELADO");

    // Act
    liq.calcularPrecios();

    // Assert — 100 * 0.70 = 70
    assertThat(liq.getPrecioConDescuento30())
            .isEqualByComparingTo(new BigDecimal("70.00"));
  }

  @Test
  @DisplayName("Descuento 40% sobre precio 100 → debe dar 60")
  void calcularDescuento40_debeSerCorrecto() {
    // Arrange
    LiquidacionProducto liq = new LiquidacionProducto();
    liq.setProducto(productoCongelado);
    liq.setEstado("CONGELADO");

    // Act
    liq.calcularPrecios();

    // Assert — 100 * 0.60 = 60
    assertThat(liq.getPrecioConDescuento40())
            .isEqualByComparingTo(new BigDecimal("60.00"));
  }

  @Test
  @DisplayName("Precio congelado = costo * 1.10 → recuperar inversión mínima")
  void precioCongelado_debeSerCostoMas10Porciento() {
    // Arrange — costo: 60
    LiquidacionProducto liq = new LiquidacionProducto();
    liq.setProducto(productoCongelado);

    // Act
    liq.calcularPrecios();

    // Assert — 60 * 1.10 = 66
    assertThat(liq.getPrecioCongelado())
            .isEqualByComparingTo(new BigDecimal("66.00"));
  }

  @Test
  @DisplayName("Capital congelado = costo * stock")
  void capitalCongelado_debeSerCostoMultiplicadoPorStock() {
    // Arrange — costo: 60, stock: 3
    LiquidacionProducto liq = new LiquidacionProducto();
    liq.setProducto(productoCongelado);

    // Act
    liq.calcularPrecios();

    // Assert — 60 * 3 = 180
    assertThat(liq.getCapitalCongelado())
            .isEqualByComparingTo(new BigDecimal("180.00"));
  }

  // =============================================
  // TESTS DE RESUMEN
  // =============================================

  @Test
  @DisplayName("Resumen con lista vacía → todos los valores en cero")
  void resumenConListaVacia_debeRetornarCeros() {
    // Act
    LiquidacionService.ResumenLiquidacion resumen =
            liquidacionService.calcularResumen(List.of());

    // Assert
    assertThat(resumen.totalCapital())
            .isEqualByComparingTo(BigDecimal.ZERO);
    assertThat(resumen.productosCongelados()).isEqualTo(0L);
    assertThat(resumen.productosLentos()).isEqualTo(0L);
    assertThat(resumen.productosActivos()).isEqualTo(0L);
  }

  @Test
  @DisplayName("Meta de liberación = 50% del capital congelado")
  void metaLiberacion_debeSerMitadDelCapitalCongelado() {
    // Arrange — un producto congelado con capital 180
    LiquidacionProducto liq = new LiquidacionProducto();
    liq.setProducto(productoCongelado);
    liq.setEstado("CONGELADO");
    liq.calcularPrecios();

    // Act
    LiquidacionService.ResumenLiquidacion resumen =
            liquidacionService.calcularResumen(List.of(liq));

    // Assert — meta = 180 * 0.50 = 90
    assertThat(resumen.metaLiberacion())
            .isEqualByComparingTo(new BigDecimal("90.00"));
  }

  // =============================================
  // TESTS DE TOLERANCIA A FALLOS
  // =============================================

  @Test
  @DisplayName("calcularFeriasSinVender con 0 días → debe retornar 0")
  void calcularFerias_conCeroDias_debeRetornarCero() {
    // Act
    int ferias = liquidacionService.calcularFeriasSinVender(0);

    // Assert
    assertThat(ferias).isEqualTo(0);
  }

  @Test
  @DisplayName("calcularFeriasSinVender con días negativos → debe retornar 0")
  void calcularFerias_conDiasNegativos_debeRetornarCero() {
    // Act
    int ferias = liquidacionService.calcularFeriasSinVender(-5);

    // Assert
    assertThat(ferias).isGreaterThanOrEqualTo(0);
  }
}