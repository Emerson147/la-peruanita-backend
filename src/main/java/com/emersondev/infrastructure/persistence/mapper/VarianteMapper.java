package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.Variante;
import com.emersondev.infrastructure.persistence.entity.VarianteEntity;
import org.springframework.stereotype.Component;

@Component
public class VarianteMapper {

  public VarianteEntity toEntity(Variante variante) {
    if (variante == null) return null;

    VarianteEntity entity = new VarianteEntity();
    entity.setId(variante.getId());
    entity.setProductId(variante.getProductId());
    entity.setSize(variante.getSize());
    entity.setColor(variante.getColor());

    entity.setBarcode(variante.getBarcode());
    entity.setVersion(variante.getVersion() != null ? variante.getVersion() : 0L);

    return entity;
  }

  public Variante toDomain(VarianteEntity entity) {
    if (entity == null) return null;

    Variante variante = new Variante();
    variante.setId(entity.getId());
    variante.setProductId(entity.getProductId());
    variante.setSize(entity.getSize());
    variante.setColor(entity.getColor());

    variante.setBarcode(entity.getBarcode());
    variante.setVersion(entity.getVersion() != null ? entity.getVersion() : 0L);

    return variante;
  }
}
