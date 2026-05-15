package com.emersondev.application.usecase.producto;

import com.emersondev.domain.model.Producto;
import com.emersondev.domain.model.Variante;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.domain.repository.VarianteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrearProductoUseCase {

  private final ProductoRepository productoRepository;
  private final VarianteRepository varianteRepository;

  @Transactional
  public Producto ejecutar(Producto producto, List<Variante> variantes) {
    log.info("Creando nuevo producto: {}", producto.getName());

    // Reglas de negocio por defecto
    if (producto.getStatus() == null) {
      producto.setStatus("active");
    }
    if (producto.getMinStock() == null) {
      producto.setMinStock(5);
    }
    if (producto.getCost() == null) {
      producto.setCost(BigDecimal.ZERO);
    }

    // Stock total = suma de stocks de todas las variantes
    if (variantes != null && !variantes.isEmpty()) {
      int stockTotal = variantes.stream()
              .mapToInt(v -> v.getStock() != null ? v.getStock() : 0)
              .sum();
      producto.setStock(stockTotal);
      log.info("Stock total calculado desde variantes: {}", stockTotal);
    }

    producto.setCreatedAt(LocalDateTime.now());
    producto.setUpdatedAt(LocalDateTime.now());

    // Guardar producto primero
    Producto creado = productoRepository.save(producto);
    log.info("Producto creado con id: {}", creado.getId());

    // Guardar cada variante asociada al producto
    if (variantes != null && !variantes.isEmpty()) {
      variantes.forEach(variante -> {
        variante.setProductId(creado.getId());
        varianteRepository.save(variante);
        log.info("Variante guardada: {} - {} (stock: {})",
                variante.getSize(),
                variante.getColor(),
                variante.getStock());
      });
    }

    return creado;
  }
}