package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.Almacen;
import com.emersondev.infrastructure.persistence.entity.AlmacenEntity;
import org.springframework.stereotype.Component;

@Component
public class AlmacenMapper {

  public Almacen toDomain(AlmacenEntity entity) {
    if (entity == null) return null;
    Almacen domain = new Almacen();
    domain.setId(entity.getId());
    domain.setNombre(entity.getNombre());
    domain.setDireccion(entity.getDireccion());
    domain.setActivo(entity.isActivo());
    return domain;
  }

  public AlmacenEntity toEntity(Almacen domain) {
    if (domain == null) return null;
    AlmacenEntity entity = new AlmacenEntity();
    entity.setId(domain.getId());
    entity.setNombre(domain.getNombre());
    entity.setDireccion(domain.getDireccion());
    entity.setActivo(domain.isActivo());
    return entity;
  }
}
