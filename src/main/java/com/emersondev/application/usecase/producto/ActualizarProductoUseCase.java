package com.emersondev.application.usecase.producto;

import com.emersondev.domain.exception.ProductoNotFoundException;
import com.emersondev.domain.model.Producto;
import com.emersondev.domain.model.Variante;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.domain.repository.VarianteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActualizarProductoUseCase {

  private final ProductoRepository productoRepository;
  private final VarianteRepository varianteRepository;

  @Transactional
  public Producto ejecutar(UUID id, Producto productoActualizado, List<Variante> variantesActualizadas) {
    log.info("Actualizando producto con id: {}", id);

    Producto productoExistente = productoRepository.findById(id)
            .orElseThrow(() -> {
              log.error("Producto no encontrado con id: {}", id);
              return new ProductoNotFoundException(id.toString());
            });

    productoExistente.setName(productoActualizado.getName());
    productoExistente.setCategoria(productoActualizado.getCategoria());
    productoExistente.setBrand(productoActualizado.getBrand());
    productoExistente.setPrice(productoActualizado.getPrice());
    productoExistente.setCost(productoActualizado.getCost());
    
    // Recalcular stock si hay variantes
    if (variantesActualizadas != null && !variantesActualizadas.isEmpty()) {
      int stockTotal = variantesActualizadas.stream()
              .mapToInt(v -> v.getStock() != null ? v.getStock() : 0)
              .sum();
      productoExistente.setStock(stockTotal);
      log.info("Stock total actualizado desde variantes: {}", stockTotal);
    } else {
      productoExistente.setStock(productoActualizado.getStock());
    }

    productoExistente.setMinStock(productoActualizado.getMinStock());
    productoExistente.setSizes(productoActualizado.getSizes());
    productoExistente.setColors(productoActualizado.getColors());
    productoExistente.setImage(productoActualizado.getImage());
    productoExistente.setBarcode(productoActualizado.getBarcode());
    productoExistente.setStatus(productoActualizado.getStatus());
    productoExistente.setUpdatedAt(LocalDateTime.now());

    Producto actualizado = productoRepository.save(productoExistente);
    log.info("Producto actualizado exitosamente: {}", actualizado.getName());

    // Sincronizar variantes
    if (variantesActualizadas != null) {
      List<Variante> variantesActuales = varianteRepository.findByProductId(id);
      
      List<UUID> idsNuevos = variantesActualizadas.stream()
              .filter(v -> v.getId() != null)
              .map(Variante::getId)
              .toList();

      // Eliminar variantes que ya no pertenecen al producto
      for (Variante actual : variantesActuales) {
        if (!idsNuevos.contains(actual.getId())) {
          varianteRepository.deleteById(actual.getId());
          log.info("Variante eliminada: {}", actual.getId());
        }
      }

      // Guardar o actualizar las variantes enviadas
      for (Variante variante : variantesActualizadas) {
        variante.setProductId(id);
        varianteRepository.save(variante);
        log.info("Variante guardada/actualizada (Talla: {}, Color: {}, Stock: {})", variante.getSize(), variante.getColor(), variante.getStock());
      }
    }

    return actualizado;
  }
}