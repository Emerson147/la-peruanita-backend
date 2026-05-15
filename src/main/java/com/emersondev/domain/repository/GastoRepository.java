package com.emersondev.domain.repository;

import com.emersondev.domain.model.Gasto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GastoRepository {

  Gasto save(Gasto gasto);
  Optional<Gasto> findById(UUID id);
  List<Gasto> findAll();
  List<Gasto> findByCategory(String category);
  List<Gasto> findByFecha(LocalDateTime desde, LocalDateTime hasta);
  void deleteById(UUID id);
  boolean existsById(UUID id);
}