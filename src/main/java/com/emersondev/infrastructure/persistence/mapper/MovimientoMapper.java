package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.MovimientoInventario;
import com.emersondev.infrastructure.persistence.entity.MovimientoEntity;
import org.springframework.stereotype.Component;

@Component
public class MovimientoMapper {

  public MovimientoEntity toEntity(MovimientoInventario m) {
    if (m == null) return null;

    MovimientoEntity entity = new MovimientoEntity();
    entity.setId(m.getId());
    entity.setMovementNumber(m.getMovementNumber());
    entity.setType(m.getType());
    entity.setProductId(m.getProductId());
    entity.setVarianteId(m.getVarianteId());
    entity.setAlmacenOrigenId(m.getAlmacenOrigenId());
    entity.setAlmacenDestinoId(m.getAlmacenDestinoId());
    entity.setProductName(m.getProductName());
    entity.setQuantity(m.getQuantity());
    entity.setQuantityBefore(m.getQuantityBefore());
    entity.setQuantityAfter(m.getQuantityAfter());
    entity.setReason(m.getReason());
    entity.setSupplier(m.getSupplier());
    entity.setInvoice(m.getInvoice());
    entity.setUnitCost(m.getUnitCost());
    entity.setTotalCost(m.getTotalCost());
    entity.setCreatedBy(m.getCreatedBy());
    entity.setNotes(m.getNotes());
    entity.setCreatedAt(m.getCreatedAt());

    return entity;
  }

  public MovimientoInventario toDomain(MovimientoEntity entity) {
    if (entity == null) return null;

    MovimientoInventario m = new MovimientoInventario();
    m.setId(entity.getId());
    m.setMovementNumber(entity.getMovementNumber());
    m.setType(entity.getType());
    m.setProductId(entity.getProductId());
    m.setVarianteId(entity.getVarianteId());
    m.setAlmacenOrigenId(entity.getAlmacenOrigenId());
    m.setAlmacenDestinoId(entity.getAlmacenDestinoId());
    m.setProductName(entity.getProductName());
    m.setQuantity(entity.getQuantity());
    m.setQuantityBefore(entity.getQuantityBefore());
    m.setQuantityAfter(entity.getQuantityAfter());
    m.setReason(entity.getReason());
    m.setSupplier(entity.getSupplier());
    m.setInvoice(entity.getInvoice());
    m.setUnitCost(entity.getUnitCost());
    m.setTotalCost(entity.getTotalCost());
    m.setCreatedBy(entity.getCreatedBy());
    m.setNotes(entity.getNotes());
    m.setCreatedAt(entity.getCreatedAt());

    return m;
  }
}