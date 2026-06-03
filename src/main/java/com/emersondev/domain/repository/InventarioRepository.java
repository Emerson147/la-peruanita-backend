package com.emersondev.domain.repository;

import com.emersondev.domain.model.Inventario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventarioRepository {

  Inventario save(Inventario inventario);
  
  Optional<Inventario> findById(UUID id);
  
  Optional<Inventario> findByVarianteIdAndAlmacenId(UUID varianteId, UUID almacenId);
  
  List<Inventario> findByVarianteId(UUID varianteId);
  
  List<Inventario> findByAlmacenId(UUID almacenId);
  
}
