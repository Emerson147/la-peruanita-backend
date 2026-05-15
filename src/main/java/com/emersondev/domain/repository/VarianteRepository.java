package com.emersondev.domain.repository;

import com.emersondev.domain.model.Variante;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VarianteRepository {

  Variante save(Variante variante);

  Optional<Variante> findById(UUID id);

  List<Variante> findByProductId(UUID productId);

  void deleteByProductId(UUID productId);

  void deleteById(UUID id);

  boolean existsById(UUID id);
}
