package com.emersondev.application.usecase.venta;

import com.emersondev.domain.exception.ProductoNotFoundException;
import com.emersondev.domain.exception.StockInsuficienteException;
import com.emersondev.domain.model.Producto;
import com.emersondev.domain.model.Variante;
import com.emersondev.domain.model.Venta;
import com.emersondev.domain.model.VentaItem;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.domain.repository.VarianteRepository;
import com.emersondev.domain.repository.VentaRepository;
import com.emersondev.domain.repository.ClienteRepository;
import com.emersondev.domain.model.Cliente;
import com.emersondev.domain.service.GamificationDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrarVentaUseCase {

  private final VentaRepository ventaRepository;
  private final ProductoRepository productoRepository;
  private final VarianteRepository varianteRepository;
  private final ClienteRepository clienteRepository;
  private final GamificationDomainService gamificationDomainService;

  @Transactional
  public Venta ejecutar(Venta venta) {
    log.info("Registrando nueva venta con {} items", venta.getItems().size());

    // PASO 1 — Validar y procesar cada item
    List<VentaItem> itemsProcesados = procesarItems(venta.getItems());
    venta.setItems(itemsProcesados);

    // PASO 2 — Calcular totales
    venta.calcularSubtotal();
    venta.calcularTotal();

    // PASO 3 — Generar número de venta
    String saleNumber = generarNumerVenta();
    venta.setSaleNumber(saleNumber);

    // PASO 4 — Estado y fecha
    venta.setStatus("completed");
    venta.setCreatedAt(LocalDateTime.now());

    // PASO 5 — Guardar venta
    Venta ventaGuardada = ventaRepository.save(venta);
    log.info("Venta registrada exitosamente: {}", saleNumber);

    // Verificar logro si hay vendedorId
    if (venta.getVendedorId() != null) {
      try {
        gamificationDomainService.verificarLogros(
                venta.getVendedorId(), ventaGuardada
        );
      } catch (Exception e) {
        log.warn("Error verificando logros: {} ", e.getMessage());
      }
    }

    // PASO 5.5 — Verificar cliente y actualizar su LTV (Monto Gastado/Jerarquía)
    if (venta.getCustomerId() != null) {
      try {
        clienteRepository.findById(venta.getCustomerId()).ifPresent(cliente -> {
          cliente.registrarCompra(venta.getTotal());
          clienteRepository.save(cliente);
          log.info("LTV actualizado para el cliente {}", cliente.getId());
        });
      } catch (Exception e) {
        log.warn("Error actualizando LTV del cliente: {}", e.getMessage());
      }
    }

    // PASO 6 — Descontar stock de cada producto
    descontarStock(itemsProcesados);

    return ventaGuardada;
  }

  private List<VentaItem> procesarItems(List<VentaItem> items) {
    return items.stream().map(item -> {

      // Verificar que el producto existe
      Producto producto = productoRepository
              .findById(item.getProductId())
              .orElseThrow(() -> new ProductoNotFoundException(
                      item.getProductId().toString()));

      // Si tiene varianteId — verificar stock de la variante
      if (item.getVarianteId() != null) {
        Variante variante = varianteRepository
                .findById(item.getVarianteId())
                .orElseThrow(() -> new RuntimeException(
                        "Variante no encontrada: " + item.getVarianteId()));

        if (variante.getStock() < item.getQuantity()) {
          log.error("Stock insuficiente variante {}-{}",
                  variante.getSize(), variante.getColor());
          throw new StockInsuficienteException(
                  producto.getName() + " talla " +
                          variante.getSize() + " color " + variante.getColor());
        }

        item.setSize(variante.getSize());
        item.setColor(variante.getColor());

      } else {
        // Sin variante — verificar stock general del producto
        if (producto.getStock() < item.getQuantity()) {
          throw new StockInsuficienteException(producto.getName());
        }
      }

      item.setProductName(producto.getName());
      item.setUnitPrice(producto.getPrice());
      item.calcularSubtotal();

      return item;
    }).toList();
  }

  private void descontarStock(List<VentaItem> items) {
    items.forEach(item -> {

      // Si tiene variante — descontar de la variante específica
      if (item.getVarianteId() != null) {
        Variante variante = varianteRepository
                .findById(item.getVarianteId())
                .orElseThrow(() -> new RuntimeException(
                        "Variante no encontrada al descontar stock"));

        int stockAnterior = variante.getStock();
        variante.descontarStock(item.getQuantity());
        varianteRepository.save(variante);

        // Descontar también del stock general del producto
        Producto producto = productoRepository
                .findById(item.getProductId())
                .orElseThrow(() -> new ProductoNotFoundException(
                        item.getProductId().toString()));
        int stockProductoAnterior = producto.getStock();
        producto.descontarStock(item.getQuantity());
        productoRepository.save(producto);

        log.info("Stock variante actualizado - {}/{} | Antes: {} | Después: {}",
                variante.getSize(), variante.getColor(),
                stockAnterior, variante.getStock());
        log.info("Stock general del producto actualizado - {} | Antes: {} | Después: {}",
                producto.getName(), stockProductoAnterior, producto.getStock());

      } else {
        // Sin variante — descontar del producto general
        Producto producto = productoRepository
                .findById(item.getProductId())
                .orElseThrow(() -> new ProductoNotFoundException(
                        item.getProductId().toString()));

        int stockAnterior = producto.getStock();
        producto.descontarStock(item.getQuantity());
        productoRepository.save(producto);

        log.info("Stock producto actualizado - {} | Antes: {} | Después: {}",
                producto.getName(), stockAnterior, producto.getStock());
      }
    });
  }

  private String generarNumerVenta() {
    Long total = ventaRepository.countAll();
    return String.format("VENTA-%04d", total + 1);
    // Genera: VENTA-0001, VENTA-0002, etc.
  }
}
