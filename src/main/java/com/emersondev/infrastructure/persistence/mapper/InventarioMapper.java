package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.Inventario;
import com.emersondev.infrastructure.persistence.entity.InventarioEntity;
import org.springframework.stereotype.Component;

@Component
public class InventarioMapper {

  public Inventario toDomain(InventarioEntity entity) {
    if (entity == null) return null;

    Inventario domain = new Inventario();
    domain.setId(entity.getId());
    domain.setVarianteId(entity.getVarianteId());
    
    if (entity.getAlmacen() != null) {
      domain.setAlmacenId(entity.getAlmacen().getId());
      domain.setNombreAlmacen(entity.getAlmacen().getNombre());
    }
    
    domain.setStock(entity.getStock());
    domain.setMinStock(entity.getMinStock());

    return domain;
  }

  public InventarioEntity toEntity(Inventario domain) {
    if (domain == null) return null;

    InventarioEntity entity = new InventarioEntity();
    entity.setId(domain.getId());
    entity.setVarianteId(domain.getVarianteId());
    entity.setStock(domain.getStock());
    entity.setMinStock(domain.getMinStock());

    // Nota: El AlmacenEntity debe ser seteado en el Adapter
    return entity;
  }
}
