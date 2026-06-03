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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrearProductoUseCase {

  private final ProductoRepository productoRepository;
  private final VarianteRepository varianteRepository;
  private final com.emersondev.domain.repository.InventarioRepository inventarioRepository;
  private final com.emersondev.domain.repository.AlmacenRepository almacenRepository;

  @Transactional
  public Producto ejecutar(Producto producto, List<Variante> variantes) {
    log.info("Creando nuevo producto: {}", producto.getName());

    // Reglas de negocio por defecto
    if (producto.getStatus() == null) {
      producto.setStatus("active");
    }

    if (producto.getCost() == null) {
      producto.setCost(BigDecimal.ZERO);
    }

    producto.setCreatedAt(LocalDateTime.now());
    producto.setUpdatedAt(LocalDateTime.now());

    // Guardar producto primero
    Producto creado = productoRepository.save(producto);
    log.info("Producto creado con id: {}", creado.getId());

    // Guardar cada variante asociada al producto con sus inventarios
    if (variantes != null && !variantes.isEmpty()) {
      variantes.forEach(variante -> {
        variante.setProductId(creado.getId());
        Variante varCreada = varianteRepository.save(variante);
        log.info("Variante guardada: {} - {}",
                variante.getSize(),
                variante.getColor());

        if (variante.getInventarios() != null) {
          variante.getInventarios().forEach(inv -> {
            inv.setVarianteId(varCreada.getId());
            UUID resolvedAlmacenId = resolverAlmacen(inv.getAlmacenId(), inv.getNombreAlmacen());
            inv.setAlmacenId(resolvedAlmacenId);
            inventarioRepository.save(inv);
            log.info("Inventario registrado para almacén: {}, stock: {}", resolvedAlmacenId, inv.getStock());
          });
        }
      });
    }

    return creado;
  }

  private UUID resolverAlmacen(UUID almacenId, String nombreAlmacen) {
    if (almacenId != null) {
      return almacenRepository.findById(almacenId)
              .map(com.emersondev.domain.model.Almacen::getId)
              .orElseGet(() -> {
                com.emersondev.domain.model.Almacen nAlmacen = new com.emersondev.domain.model.Almacen();
                nAlmacen.setId(almacenId);
                nAlmacen.setNombre(nombreAlmacen != null ? nombreAlmacen : "Almacén Central");
                nAlmacen.setDireccion("Dirección por defecto");
                nAlmacen.setActivo(true);
                return almacenRepository.save(nAlmacen).getId();
              });
    }
    
    if (nombreAlmacen != null && !nombreAlmacen.trim().isEmpty()) {
      return almacenRepository.findByNombre(nombreAlmacen)
              .map(com.emersondev.domain.model.Almacen::getId)
              .orElseGet(() -> {
                com.emersondev.domain.model.Almacen nAlmacen = new com.emersondev.domain.model.Almacen();
                nAlmacen.setNombre(nombreAlmacen);
                nAlmacen.setDireccion("Dirección por defecto");
                nAlmacen.setActivo(true);
                return almacenRepository.save(nAlmacen).getId();
              });
    }

    return almacenRepository.findByNombre("Almacén Central")
            .map(com.emersondev.domain.model.Almacen::getId)
            .orElseGet(() -> {
              com.emersondev.domain.model.Almacen nAlmacen = new com.emersondev.domain.model.Almacen();
              nAlmacen.setNombre("Almacén Central");
              nAlmacen.setDireccion("Dirección Principal");
              nAlmacen.setActivo(true);
              return almacenRepository.save(nAlmacen).getId();
            });
  }
}