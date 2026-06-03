package com.emersondev.application.usecase.venta;

import com.emersondev.domain.exception.VentaNotFoundException;
import com.emersondev.domain.model.Venta;
import com.emersondev.domain.repository.VentaRepository;
import com.emersondev.domain.repository.InventarioRepository;
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
  private final InventarioRepository inventarioRepository;

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
      if (item.getVarianteId() != null && venta.getAlmacenId() != null) {
          inventarioRepository.findByVarianteIdAndAlmacenId(item.getVarianteId(), venta.getAlmacenId())
              .ifPresent(inv -> {
                  inv.setStock(inv.getStock() + item.getQuantity());
                  inventarioRepository.save(inv);
                  log.info("Stock devuelto a Inventario - Variante: {}, Almacén: {} | +{}", item.getVarianteId(), venta.getAlmacenId(), item.getQuantity());
              });
      } else {
          log.warn("No se pudo devolver el stock: Variante o Almacén nulo para el item {}", item.getId());
      }
    });

    // Cambiar estado
    venta.setStatus("cancelled");
    Venta cancelada = ventaRepository.save(venta);
    log.info("Venta {} cancelada exitosamente", venta.getSaleNumber());

    return cancelada;
  }
}
