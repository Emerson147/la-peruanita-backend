package com.emersondev.application.usecase.movimiento;

import com.emersondev.domain.exception.ProductoNotFoundException;
import com.emersondev.domain.model.MovimientoInventario;
import com.emersondev.domain.model.Producto;
import com.emersondev.domain.model.Variante;
import com.emersondev.domain.repository.MovimientoRepository;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.domain.repository.VarianteRepository;
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

    // Si tiene variante — actualizar stock de variante
    if (movimiento.getVarianteId() != null) {
      Variante variante = varianteRepository
              .findById(movimiento.getVarianteId())
              .orElseThrow(() -> new RuntimeException(
                      "Variante no encontrada"));

      int stockAntes = variante.getStock();
      int stockDespues = calcularStockDespues(
              movimiento.getType(),
              stockAntes,
              movimiento.getQuantity());

      movimiento.setQuantityBefore(stockAntes);
      movimiento.setQuantityAfter(stockDespues);

      variante.setStock(stockDespues);
      varianteRepository.save(variante);

      log.info("Stock variante {}-{}: {} → {}",
              variante.getSize(), variante.getColor(),
              stockAntes, stockDespues);

    } else {
      // Sin variante — actualizar stock del producto
      int stockAntes = producto.getStock();
      int stockDespues = calcularStockDespues(
              movimiento.getType(),
              stockAntes,
              movimiento.getQuantity());

      movimiento.setQuantityBefore(stockAntes);
      movimiento.setQuantityAfter(stockDespues);

      producto.setStock(stockDespues);
      productoRepository.save(producto);

      log.info("Stock producto {}: {} → {}",
              producto.getName(), stockAntes, stockDespues);
    }

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

  private int calcularStockDespues(
          String type, int stockActual, int cantidad) {
    return switch (type) {
      case "entrada" -> stockActual + cantidad;
      case "ajuste"  -> cantidad;
      default -> stockActual;
    };
  }

  private String generarNumero(String type) {
    String prefijo = "entrada".equals(type) ? "ENTR" : "AJUS";
    long count = movimientoRepository.countByType(type);
    return String.format("%s-%04d", prefijo, count + 1);
  }
}