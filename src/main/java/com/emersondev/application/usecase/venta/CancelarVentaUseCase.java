package com.emersondev.application.usecase.venta;

import com.emersondev.domain.exception.VentaNotFoundException;
import com.emersondev.domain.model.Producto;
import com.emersondev.domain.model.Venta;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.domain.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelarVentaUseCase {

  private final VentaRepository ventaRepository;
  private final ProductoRepository productoRepository;

  @Transactional
  public Venta ejecutar(UUID id) {
    log.info("Cancelando venta con id: {}", id);

    // Verificar que existe
    Venta venta = ventaRepository.findById(id)
            .orElseThrow(() -> new VentaNotFoundException(id.toString()));

    // Verificar que se puede cancelar
    if (!venta.sePuedeCancelar()) {
      throw new RuntimeException(
              "La venta " + venta.getSaleNumber() + " no se puede cancelar");
    }

    // Devolver stock a cada producto
    venta.getItems().forEach(item -> {
      Producto producto = productoRepository
              .findById(item.getProductId())
              .orElseThrow(() -> new RuntimeException(
                      "Producto no encontrado al cancelar venta"));

      producto.setStock(producto.getStock() + item.getQuantity());
      productoRepository.save(producto);

      log.info("Stock devuelto - Producto: {} | +{}",
              producto.getName(), item.getQuantity());
    });

    // Cambiar estado
    venta.setStatus("cancelled");
    Venta cancelada = ventaRepository.save(venta);
    log.info("Venta {} cancelada exitosamente", venta.getSaleNumber());

    return cancelada;
  }
}
