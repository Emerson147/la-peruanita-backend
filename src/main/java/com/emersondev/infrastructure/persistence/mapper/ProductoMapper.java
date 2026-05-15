package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.Producto;
import com.emersondev.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

  // Convierte de Dominio → Entity (para guardar en BD)
  public ProductoEntity toEntity(Producto producto) {
    if (producto == null) return null;

    ProductoEntity entity = new ProductoEntity();
    entity.setId(producto.getId());
    entity.setName(producto.getName());
    entity.setCategoria(producto.getCategoria());
    entity.setBrand(producto.getBrand());
    entity.setPrice(producto.getPrice());
    entity.setCost(producto.getCost());
    entity.setStock(producto.getStock());
    entity.setMinStock(producto.getMinStock());
    entity.setSizes(producto.getSizes());
    entity.setColors(producto.getColors());
    entity.setImage(producto.getImage());
    entity.setBarcode(producto.getBarcode());
    entity.setStatus(producto.getStatus());
    entity.setVersion(producto.getVersion() != null ? producto.getVersion() : 0L);
    entity.setCreatedAt(producto.getCreatedAt());
    entity.setUpdatedAt(producto.getUpdatedAt());

    return entity;
  }

  // Convierte de Entity → Dominio (para devolver al negocio)
  public Producto toDomain(ProductoEntity entity) {
    if (entity == null) return null;

    Producto producto = new Producto();
    producto.setId(entity.getId());
    producto.setName(entity.getName());
    producto.setCategoria(entity.getCategoria());
    producto.setBrand(entity.getBrand());
    producto.setPrice(entity.getPrice());
    producto.setCost(entity.getCost());
    producto.setStock(entity.getStock());
    producto.setMinStock(entity.getMinStock());
    producto.setSizes(entity.getSizes());
    producto.setColors(entity.getColors());
    producto.setImage(entity.getImage());
    producto.setBarcode(entity.getBarcode());
    producto.setStatus(entity.getStatus());
    producto.setVersion(entity.getVersion() != null ? entity.getVersion() : 0L);
    producto.setCreatedAt(entity.getCreatedAt());
    producto.setUpdatedAt(entity.getUpdatedAt());

    return producto;
  }
}