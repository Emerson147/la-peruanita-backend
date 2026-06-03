package com.emersondev.application.usecase.movimiento;

import com.emersondev.domain.exception.ProductoNotFoundException;
import com.emersondev.domain.model.Inventario;
import com.emersondev.domain.model.MovimientoInventario;
import com.emersondev.domain.model.Producto;
import com.emersondev.domain.model.Variante;
import com.emersondev.domain.repository.MovimientoRepository;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.domain.repository.VarianteRepository;
import com.emersondev.domain.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrarMovimientoUseCase {

  private final MovimientoRepository movimientoRepository;
  private final ProductoRepository productoRepository;
  private final InventarioRepository inventarioRepository;
  private final VarianteRepository varianteRepository;

  @Transactional
  public MovimientoInventario ejecutar(
          MovimientoInventario movimiento) {

    log.info("Registrando movimiento tipo: {} para producto: {}",
            movimiento.getType(), movimiento.getProductId());

    // Verificar que el producto existe
    Producto producto = productoRepository
            .findById(movimiento.getProductId())
            .orElseThrow(() -> new ProductoNotFoundException(
                    movimiento.getProductId().toString()));

    movimiento.setProductName(producto.getName());

    // Si no tiene variante, arrojar error
    if (movimiento.getVarianteId() == null || movimiento.getAlmacenOrigenId() == null) {
      throw new IllegalArgumentException("Movimiento requiere Variante y Almacén Origen.");
    }

    Variante variante = varianteRepository
            .findById(movimiento.getVarianteId())
            .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

    Inventario inv = inventarioRepository
            .findByVarianteIdAndAlmacenId(variante.getId(), movimiento.getAlmacenOrigenId())
            .orElseGet(() -> {
                Inventario newInv = new Inventario();
                newInv.setVarianteId(variante.getId());
                newInv.setAlmacenId(movimiento.getAlmacenOrigenId());
                newInv.setStock(0);
                newInv.setMinStock(5);
                return newInv;
            });

    movimiento.setQuantityBefore(inv.getStock());

    int cantidad = movimiento.getQuantity() != null ? movimiento.getQuantity() : 0;
    
    if ("entrada".equals(movimiento.getType())) {
      inv.setStock(inv.getStock() + cantidad);
    } else if ("ajuste".equals(movimiento.getType())) {
      inv.setStock(cantidad); // Asumo que ajuste es SET, si es sumar/restar ajustar aquí
    } else if ("salida".equals(movimiento.getType())) {
      inv.descontarStock(cantidad);
    }

    inventarioRepository.save(inv);
    movimiento.setQuantityAfter(inv.getStock());

    // Calcular costo total
    movimiento.calcularTotalCost();

    // Generar número de movimiento
    movimiento.setMovementNumber(
            generarNumero(movimiento.getType()));

    movimiento.setCreatedAt(LocalDateTime.now());

    MovimientoInventario guardado =
            movimientoRepository.save(movimiento);

    log.info("Movimiento registrado: {}",
            guardado.getMovementNumber());

    return guardado;
  }



  private String generarNumero(String type) {
    String prefijo = "entrada".equals(type) ? "ENTR" : "AJUS";
    long count = movimientoRepository.countByType(type);
    return String.format("%s-%04d", prefijo, count + 1);
  }
}