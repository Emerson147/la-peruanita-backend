package com.emersondev.domain.repository;

import com.emersondev.domain.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductoRepository {

  // Guardar o actualizar un producto
  Producto save(Producto producto);

  // Buscar por ID
  Optional<Producto> findById(UUID id);

  // Traer todos los productos
  List<Producto> findAll();

  // Traer por categoría
  List<Producto> findByCategory(String category);

  // Traer solo los activos
  List<Producto> findByStatus(String status);

  // Traer productos con stock bajo
  List<Producto> findByStockBajo();

  // Eliminar por ID
  void deleteById(UUID id);

  // Verificar si existe
  boolean existsById(UUID id);

  //Paginados
  Page<Producto> findAllPaginado(Pageable pageable);
  Page<Producto> findByCategoriaPaginado(String category, Pageable pageable);
}