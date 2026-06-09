package com.emersondev.application.usecase.producto;

import com.emersondev.domain.model.Almacen;
import com.emersondev.domain.model.Inventario;
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
    producto.asignarValoresPorDefectoCreacion();

    // Guardar producto primero
    Producto creado = productoRepository.save(producto);
    log.info("Producto creado con id: {}", creado.getId());

    // Guardar cada variante asociada al producto con sus inventarios
    if (variantes != null && !variantes.isEmpty()) {
      List<Variante> variantesGuardadas = variantes.stream().map(variante -> {
        variante.setProductId(creado.getId());
        Variante varCreada = varianteRepository.save(variante);
        log.info("Variante guardada: {} - {}", varCreada.getSize(), varCreada.getColor());

        if (variante.getInventarios() != null) {
          varCreada.setInventarios(variante.getInventarios().stream().map(inv -> {
            if (inv.getMinStock() == null) {
              inv.setMinStock(5); // Regla de negocio movida al UseCase
            }
            inv.setVarianteId(varCreada.getId());
            Almacen almacenReal = resolverAlmacen(inv.getAlmacenId(), inv.getNombreAlmacen());
            inv.setAlmacenId(almacenReal.getId());
            inv.setNombreAlmacen(almacenReal.getNombre());
            Inventario invCreado = inventarioRepository.save(inv);
            log.info("Inventario registrado para almacén: {}, stock: {}", almacenReal.getId(), invCreado.getStock());
            return invCreado;
          }).toList());
        }
        return varCreada;
      }).toList();

      creado.asignarVariantes(variantesGuardadas);
    }

    return creado;
  }

  private Almacen resolverAlmacen(UUID almacenId, String nombreAlmacen) {
    if (almacenId != null) {
      return almacenRepository.findById(almacenId)
              .orElseGet(() -> {
                Almacen nAlmacen = new Almacen();
                nAlmacen.setId(almacenId);
                nAlmacen.setNombre(nombreAlmacen != null ? nombreAlmacen : "Almacén Central");
                nAlmacen.setDireccion("Dirección por defecto");
                nAlmacen.setActivo(true);
                return almacenRepository.save(nAlmacen);
              });
    }

    if (nombreAlmacen != null && !nombreAlmacen.trim().isEmpty()) {
      return almacenRepository.findByNombre(nombreAlmacen)
              .orElseGet(() -> {
                Almacen nAlmacen = new Almacen();
                nAlmacen.setNombre(nombreAlmacen);
                nAlmacen.setDireccion("Dirección por defecto");
                nAlmacen.setActivo(true);
                return almacenRepository.save(nAlmacen);
              });
    }

    return almacenRepository.findByNombre("Almacén Central")
            .orElseGet(() -> {
              Almacen nAlmacen = new Almacen();
              nAlmacen.setNombre("Almacén Central");
              nAlmacen.setDireccion("Dirección Principal");
              nAlmacen.setActivo(true);
              return almacenRepository.save(nAlmacen);
            });
  }
}