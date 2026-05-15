package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.Venta;
import com.emersondev.domain.model.VentaItem;
import com.emersondev.infrastructure.persistence.entity.VentaEntity;
import com.emersondev.infrastructure.persistence.entity.VentaItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VentaMapper {

  // Dominio → Entity
  public VentaEntity toEntity(Venta venta) {
    if (venta == null) return null;

    VentaEntity entity = new VentaEntity();
    entity.setId(venta.getId());
    entity.setSaleNumber(venta.getSaleNumber());
    entity.setUserId(venta.getUserId());
    entity.setVendedorId(venta.getVendedorId());
    entity.setCustomerId(venta.getCustomerId());
    entity.setSubtotal(venta.getSubtotal());
    entity.setDiscount(venta.getDiscount());
    entity.setTax(venta.getTax());
    entity.setTotal(venta.getTotal());
    entity.setPaymentMethod(venta.getPaymentMethod());
    entity.setStatus(venta.getStatus());
    entity.setNotes(venta.getNotes());
    entity.setCreatedBy(venta.getCreatedBy());
    entity.setCreatedAt(venta.getCreatedAt());

    // Convertir items
    if (venta.getItems() != null) {
      List<VentaItemEntity> itemEntities = venta.getItems()
              .stream()
              .map(item -> toItemEntity(item, entity))
              .toList();
      entity.setItems(itemEntities);
    }

    return entity;
  }

  // Entity → Dominio
  public Venta toDomain(VentaEntity entity) {
    if (entity == null) return null;

    Venta venta = new Venta();
    venta.setId(entity.getId());
    venta.setSaleNumber(entity.getSaleNumber());
    venta.setUserId(entity.getUserId());
    venta.setVendedorId(entity.getVendedorId());
    venta.setCustomerId(entity.getCustomerId());
    venta.setSubtotal(entity.getSubtotal());
    venta.setDiscount(entity.getDiscount());
    venta.setTax(entity.getTax());
    venta.setTotal(entity.getTotal());
    venta.setPaymentMethod(entity.getPaymentMethod());
    venta.setStatus(entity.getStatus());
    venta.setNotes(entity.getNotes());
    venta.setCreatedBy(entity.getCreatedBy());
    venta.setCreatedAt(entity.getCreatedAt());

    // Convertir items
    if (entity.getItems() != null) {
      List<VentaItem> items = entity.getItems()
              .stream()
              .map(this::toItemDomain)
              .toList();
      venta.setItems(items);
    }

    return venta;
  }

  // VentaItem dominio → VentaItemEntity
  private VentaItemEntity toItemEntity(VentaItem item, VentaEntity ventaEntity) {
    VentaItemEntity entity = new VentaItemEntity();
    entity.setId(item.getId());
    entity.setVenta(ventaEntity);
    entity.setProductId(item.getProductId());
    entity.setProductName(item.getProductName());
    entity.setQuantity(item.getQuantity());
    entity.setSize(item.getSize());
    entity.setColor(item.getColor());
    entity.setUnitPrice(item.getUnitPrice());
    entity.setSubtotal(item.getSubtotal());
    return entity;
  }

  // VentaItemEntity → VentaItem dominio
  private VentaItem toItemDomain(VentaItemEntity entity) {
    VentaItem item = new VentaItem();
    item.setId(entity.getId());
    item.setSaleId(entity.getVenta().getId());
    item.setProductId(entity.getProductId());
    item.setProductName(entity.getProductName());
    item.setQuantity(entity.getQuantity());
    item.setSize(entity.getSize());
    item.setColor(entity.getColor());
    item.setUnitPrice(entity.getUnitPrice());
    item.setSubtotal(entity.getSubtotal());
    return item;
  }
}
