package com.emersondev.domain.repository;

import com.emersondev.domain.model.Almacen;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlmacenRepository {
  Almacen save(Almacen almacen);
  Optional<Almacen> findById(UUID id);
  Optional<Almacen> findByNombre(String nombre);
  List<Almacen> findAll();
}
