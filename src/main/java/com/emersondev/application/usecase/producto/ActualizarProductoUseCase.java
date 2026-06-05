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
  private final com.emersondev.domain.repository.InventarioRepository inventarioRepository;
  private final com.emersondev.domain.repository.AlmacenRepository almacenRepository;

  @Transactional
  public Producto ejecutar(UUID id, Producto productoActualizado, List<Variante> variantesActualizadas) {
    log.info("Actualizando producto con id: {}", id);

    Producto productoExistente = productoRepository.findById(id)
            .orElseThrow(() -> {
              log.error("Producto no encontrado con id: {}", id);
              return new ProductoNotFoundException(id.toString());
            });

    // Ya no se actualiza stock general ni minStock desde aquí, se maneja en Inventario
    productoExistente.actualizarDatos(productoActualizado);

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

      // Guardar o actualizar las variantes enviadas con sus inventarios
      for (Variante variante : variantesActualizadas) {
        variante.setProductId(id);
        Variante varGuardada = varianteRepository.save(variante);
        log.info("Variante guardada/actualizada (Talla: {}, Color: {})", variante.getSize(), variante.getColor());

        if (variante.getInventarios() != null) {
          for (com.emersondev.domain.model.Inventario inv : variante.getInventarios()) {
            inv.setVarianteId(varGuardada.getId());
            UUID resolvedAlmacenId = resolverAlmacen(inv.getAlmacenId(), inv.getNombreAlmacen());
            inv.setAlmacenId(resolvedAlmacenId);
            inventarioRepository.save(inv);
          }
        }
      }
    }

    return actualizado;
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