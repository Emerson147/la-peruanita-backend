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

    entity.setSizes(producto.getSizes());
    entity.setColors(producto.getColors());
    entity.setImage(producto.getImage());
    entity.setBarcode(producto.getBarcode());
    entity.setCodigoInterno(producto.getCodigoInterno());

    entity.setStatus(producto.getStatus());
    entity.setVersion(producto.getVersion() != null ? producto.getVersion() : 0L);
    entity.setCreatedAt(producto.getCreatedAt());
    entity.setUpdatedAt(producto.getUpdatedAt());

    return entity;
  }

  // Convierte de Entity → Dominio (para devolver al negocio)
  public Producto toDomain(ProductoEntity entity) {
    if (entity == null) return null;

    return Producto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .categoria(entity.getCategoria())
        .brand(entity.getBrand())
        .price(entity.getPrice())
        .cost(entity.getCost())
        .sizes(entity.getSizes())
        .colors(entity.getColors())
        .image(entity.getImage())
        .barcode(entity.getBarcode())
        .codigoInterno(entity.getCodigoInterno())
        .status(entity.getStatus())
        .version(entity.getVersion() != null ? entity.getVersion() : 0L)
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}