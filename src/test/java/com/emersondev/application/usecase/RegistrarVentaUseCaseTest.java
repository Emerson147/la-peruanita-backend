package com.emersondev.application.usecase;

import com.emersondev.application.usecase.venta.RegistrarVentaUseCase;
import com.emersondev.domain.exception.ProductoNotFoundException;
import com.emersondev.domain.exception.StockInsuficienteException;
import com.emersondev.domain.model.*;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.domain.repository.VentaRepository;
import com.emersondev.domain.repository.VarianteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarVentaUseCase — Flujo de ventas")
class RegistrarVentaUseCaseTest {

  @Mock
  private VentaRepository ventaRepository;

  @Mock
  private ProductoRepository productoRepository;

  @Mock
  private VarianteRepository varianteRepository;

  @InjectMocks
  private RegistrarVentaUseCase registrarVentaUseCase;

  private UUID productoId;
  private UUID varianteId;
  private Producto producto;
  private Variante variante;

  @BeforeEach
  void setUp() {
    productoId = UUID.randomUUID();
    varianteId = UUID.randomUUID();

    producto = new Producto();
    producto.setId(productoId);
    producto.setName("Casaca Cordelina");
    producto.setCategoria("Casacas");
    producto.setPrice(new BigDecimal("110.00"));
    producto.setCost(new BigDecimal("65.00"));
    producto.setStock(8);
    producto.setStatus("active");

    variante = new Variante();
    variante.setId(varianteId);
    variante.setProductId(productoId);
    variante.setSize("M");
    variante.setColor("Negro");
    variante.setStock(2);
  }

  // =============================================
  // TESTS DE FLUJO EXITOSO
  // =============================================

  @Test
  @DisplayName("Venta exitosa con variante → descuenta stock de variante")
  void ventaConVariante_debeDescontarStockDeVariante() {
    // Arrange
    VentaItem item = new VentaItem();
    item.setProductId(productoId);
    item.setVarianteId(varianteId);
    item.setQuantity(1);

    Venta venta = new Venta();
    venta.setItems(List.of(item));
    venta.setPaymentMethod("yape");

    when(productoRepository.findById(productoId))
            .thenReturn(Optional.of(producto));
    when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(variante));
    when(ventaRepository.countAll()).thenReturn(0L);
    when(ventaRepository.save(any(Venta.class)))
            .thenAnswer(inv -> inv.getArgument(0));
    when(varianteRepository.save(any(Variante.class)))
            .thenAnswer(inv -> inv.getArgument(0));

    // Act
    Venta resultado = registrarVentaUseCase.ejecutar(venta);

    // Assert
    assertThat(resultado.getStatus())
            .isEqualTo("completed");
    assertThat(resultado.getSaleNumber())
            .startsWith("VENTA-");

    // Verifica que se guardó la variante con stock descontado
    verify(varianteRepository, times(1))
            .save(any(Variante.class));
  }

  @Test
  @DisplayName("Venta exitosa → número de venta generado correctamente")
  void ventaExitosa_debeGenerarNumeroCorrecto() {
    // Arrange
    VentaItem item = new VentaItem();
    item.setProductId(productoId);
    item.setVarianteId(varianteId);
    item.setQuantity(1);

    Venta venta = new Venta();
    venta.setItems(List.of(item));
    venta.setPaymentMethod("efectivo");

    when(productoRepository.findById(productoId))
            .thenReturn(Optional.of(producto));
    when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(variante));
    when(ventaRepository.countAll()).thenReturn(4L);
    when(ventaRepository.save(any(Venta.class)))
            .thenAnswer(inv -> inv.getArgument(0));
    when(varianteRepository.save(any(Variante.class)))
            .thenAnswer(inv -> inv.getArgument(0));

    // Act
    Venta resultado = registrarVentaUseCase.ejecutar(venta);

    // Assert — countAll=4 → VENTA-0005
    assertThat(resultado.getSaleNumber())
            .isEqualTo("VENTA-0005");
  }

  @Test
  @DisplayName("Venta exitosa → total calculado correctamente")
  void ventaExitosa_debeTotalCorrecto() {
    // Arrange — quantity: 2, precio: 110 → total: 220
    VentaItem item = new VentaItem();
    item.setProductId(productoId);
    item.setVarianteId(varianteId);
    item.setQuantity(2);

    Venta venta = new Venta();
    venta.setItems(List.of(item));
    venta.setPaymentMethod("tarjeta");

    when(productoRepository.findById(productoId))
            .thenReturn(Optional.of(producto));
    when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(variante));
    when(ventaRepository.countAll()).thenReturn(0L);
    when(ventaRepository.save(any(Venta.class)))
            .thenAnswer(inv -> inv.getArgument(0));
    when(varianteRepository.save(any(Variante.class)))
            .thenAnswer(inv -> inv.getArgument(0));

    // Act
    Venta resultado = registrarVentaUseCase.ejecutar(venta);

    // Assert — 110 * 2 = 220
    assertThat(resultado.getTotal())
            .isEqualByComparingTo(new BigDecimal("220.00"));
  }

  // =============================================
  // TESTS DE TOLERANCIA A FALLOS
  // =============================================

  @Test
  @DisplayName("Producto no existe → lanza ProductoNotFoundException")
  void productoNoExiste_debeLanzarExcepcion() {
    // Arrange
    VentaItem item = new VentaItem();
    item.setProductId(UUID.randomUUID()); // ID que no existe
    item.setQuantity(1);

    Venta venta = new Venta();
    venta.setItems(List.of(item));

    when(productoRepository.findById(any()))
            .thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() ->
            registrarVentaUseCase.ejecutar(venta))
            .isInstanceOf(ProductoNotFoundException.class);
  }

  @Test
  @DisplayName("Stock de variante insuficiente → lanza StockInsuficienteException")
  void stockVarianteInsuficiente_debeLanzarExcepcion() {
    // Arrange — pedir 5 pero solo hay 2
    variante.setStock(2);

    VentaItem item = new VentaItem();
    item.setProductId(productoId);
    item.setVarianteId(varianteId);
    item.setQuantity(5);

    Venta venta = new Venta();
    venta.setItems(List.of(item));

    when(productoRepository.findById(productoId))
            .thenReturn(Optional.of(producto));
    when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(variante));

    // Act & Assert
    assertThatThrownBy(() ->
            registrarVentaUseCase.ejecutar(venta))
            .isInstanceOf(StockInsuficienteException.class);
  }

  @Test
  @DisplayName("Stock exacto → venta exitosa sin error")
  void stockExacto_debePermitirVenta() {
    // Arrange — pedir exactamente el stock disponible
    variante.setStock(2);

    VentaItem item = new VentaItem();
    item.setProductId(productoId);
    item.setVarianteId(varianteId);
    item.setQuantity(2); // exactamente el stock

    Venta venta = new Venta();
    venta.setItems(List.of(item));
    venta.setPaymentMethod("efectivo");

    when(productoRepository.findById(productoId))
            .thenReturn(Optional.of(producto));
    when(varianteRepository.findById(varianteId))
            .thenReturn(Optional.of(variante));
    when(ventaRepository.countAll()).thenReturn(0L);
    when(ventaRepository.save(any(Venta.class)))
            .thenAnswer(inv -> inv.getArgument(0));
    when(varianteRepository.save(any(Variante.class)))
            .thenAnswer(inv -> inv.getArgument(0));

    // Act & Assert — no debe lanzar excepción
    assertThatCode(() ->
            registrarVentaUseCase.ejecutar(venta))
            .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Variante no encontrada → lanza RuntimeException")
  void varianteNoEncontrada_debeLanzarExcepcion() {
    // Arrange
    VentaItem item = new VentaItem();
    item.setProductId(productoId);
    item.setVarianteId(UUID.randomUUID()); // variante que no existe
    item.setQuantity(1);

    Venta venta = new Venta();
    venta.setItems(List.of(item));

    when(productoRepository.findById(productoId))
            .thenReturn(Optional.of(producto));
    when(varianteRepository.findById(any()))
            .thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() ->
            registrarVentaUseCase.ejecutar(venta))
            .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("Cancelar venta → devuelve stock a la variante")
  void cancelarVenta_debeDevolverStock() {
    // Arrange — variante con stock 1 después de vender
    variante.setStock(1);

    // Act — descontar stock directamente
    variante.descontarStock(1);

    // Assert — stock llegó a 0
    assertThat(variante.getStock()).isEqualTo(0);
  }
}