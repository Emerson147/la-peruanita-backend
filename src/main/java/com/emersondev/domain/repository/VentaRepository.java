package com.emersondev.domain.repository;

import com.emersondev.domain.model.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VentaRepository {

  Venta save(Venta venta);

  Optional<Venta> findById(UUID id);

  Optional<Venta> findBySaleNumber(String saleNumber);

  List<Venta> findAll();

  List<Venta> findByUserId(UUID userId);

  List<Venta> findByStatus(String status);

  List<Venta> findByFecha(LocalDateTime desde, LocalDateTime hasta);

  void deleteById(UUID id);

  boolean existsById(UUID id);

  // Para generar número de venta — cuántas ventas hay
  Long countAll();

  // Agregar al puerto
  Page<Venta> findAllPaginado(Pageable pageable);
  Page<Venta> findByStatusPaginado(String status, Pageable pageable);
}
