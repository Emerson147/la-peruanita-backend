package com.emersondev.domain.repository;

import com.emersondev.domain.model.MovimientoInventario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovimientoRepository {

  MovimientoInventario save(MovimientoInventario movimiento);
  Optional<MovimientoInventario> findById(UUID id);
  List<MovimientoInventario> findAll();
  List<MovimientoInventario> findByProductId(UUID productId);
  List<MovimientoInventario> findByType(String type);
  List<MovimientoInventario> findByFecha(
          LocalDateTime desde, LocalDateTime hasta);
  long countByType(String type);

  Page<MovimientoInventario> findAllPaginado(Pageable pageable);
  Page<MovimientoInventario> findByTypePaginado(String type, Pageable pageable);
}