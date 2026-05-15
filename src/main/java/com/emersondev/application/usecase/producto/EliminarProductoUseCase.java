package com.emersondev.application.usecase.producto;

import com.emersondev.domain.exception.ProductoNotFoundException;
import com.emersondev.domain.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EliminarProductoUseCase {

  private final ProductoRepository productoRepository;

  public void ejecutar(UUID id) {
    log.info("Eliminando producto con id: {}", id);

    if (!productoRepository.existsById(id)) {
      log.error("Producto no encontrado con id: {}", id);
      throw new ProductoNotFoundException(id.toString());
    }

    productoRepository.deleteById(id);
    log.info("Producto eliminado exitosamente con id: {}", id);
  }
}