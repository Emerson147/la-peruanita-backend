package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.Gasto;
import com.emersondev.infrastructure.persistence.entity.GastoEntity;
import org.springframework.stereotype.Component;

@Component
public class GastoMapper {

  public GastoEntity toEntity(Gasto gasto) {
    if (gasto == null) return null;

    GastoEntity entity = new GastoEntity();
    entity.setId(gasto.getId());
    entity.setDescription(gasto.getDescription());
    entity.setAmount(gasto.getAmount());
    entity.setCategory(gasto.getCategory());
    entity.setReceipt(gasto.getReceipt());
    entity.setUserId(gasto.getUserId());
    entity.setCreatedAt(gasto.getCreatedAt());

    return entity;
  }

  public Gasto toDomain(GastoEntity entity) {
    if (entity == null) return null;

    Gasto gasto = new Gasto();
    gasto.setId(entity.getId());
    gasto.setDescription(entity.getDescription());
    gasto.setAmount(entity.getAmount());
    gasto.setCategory(entity.getCategory());
    gasto.setReceipt(entity.getReceipt());
    gasto.setUserId(entity.getUserId());
    gasto.setCreatedAt(entity.getCreatedAt());

    return gasto;
  }
}