package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductoJpaRepository extends JpaRepository<ProductoEntity, UUID> {

  // Buscar por categoría
  List<ProductoEntity> findByCategoria(String categoria);

  // Buscar por status
  List<ProductoEntity> findByStatus(String status);

  // Buscar productos con stock bajo
  @Query("SELECT p FROM ProductoEntity p WHERE p.status = 'active' AND p.id IN (SELECT v.productId FROM VarianteEntity v WHERE v.id IN (SELECT i.varianteId FROM InventarioEntity i WHERE i.stock <= i.minStock))")
  List<ProductoEntity> findProductosConStockBajo();

  //Paginados
  Page<ProductoEntity> findAll(Pageable pageable);
  Page<ProductoEntity> findByCategoria(String categoria, Pageable pageable);
}
