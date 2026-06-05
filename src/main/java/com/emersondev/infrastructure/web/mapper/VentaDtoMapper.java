package com.emersondev.infrastructure.web.mapper;

import com.emersondev.domain.model.Venta;
import com.emersondev.domain.model.VentaItem;
import com.emersondev.infrastructure.web.dto.request.VentaItemRequest;
import com.emersondev.infrastructure.web.dto.request.VentaRequest;
import com.emersondev.infrastructure.web.dto.response.VentaItemResponse;
import com.emersondev.infrastructure.web.dto.response.VentaResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VentaDtoMapper {

  // Request → Dominio
  public Venta toDomain(VentaRequest request) {
    if (request == null) return null;

    Venta venta = new Venta();
    venta.setUserId(request.getUserId());
    venta.setVendedorId(request.getVendedorId());
    venta.setCustomerId(request.getCustomerId());
    venta.setAlmacenId(request.getAlmacenId());
    venta.setDiscount(request.getDiscount());
    venta.setTax(request.getTax());
    venta.setPaymentMethod(request.getPaymentMethod());
    venta.setNotes(request.getNotes());
    venta.setCreatedBy(request.getCreatedBy());

    // Convertir items
    if (request.getItems() != null) {
      List<VentaItem> items = request.getItems()
              .stream()
              .map(this::toItemDomain)
              .toList();
      venta.setItems(items);
    }

    return venta;
  }

  // Dominio → Response
  public VentaResponse toResponse(Venta venta) {
    if (venta == null) return null;

    VentaResponse response = new VentaResponse();
    response.setId(venta.getId());
    response.setSaleNumber(venta.getSaleNumber());
    response.setUserId(venta.getUserId());
    response.setVendedorId(venta.getVendedorId());
    response.setCustomerId(venta.getCustomerId());
    response.setAlmacenId(venta.getAlmacenId());
    response.setSubtotal(venta.getSubtotal());
    response.setDiscount(venta.getDiscount());
    response.setTax(venta.getTax());
    response.setTotal(venta.getTotal());
    response.setPaymentMethod(venta.getPaymentMethod());
    response.setStatus(venta.getStatus());
    response.setNotes(venta.getNotes());
    response.setCreatedBy(venta.getCreatedBy());
    response.setCreatedAt(venta.getCreatedAt());

    // Convertir items
    if (venta.getItems() != null) {
      List<VentaItemResponse> items = venta.getItems()
              .stream()
              .map(this::toItemResponse)
              .toList();
      response.setItems(items);
      // Campo calculado — total de items
      response.setTotalItems(venta.getItems().stream()
              .mapToInt(VentaItem::getQuantity)
              .sum());
    }

    return response;
  }

  private VentaItem toItemDomain(VentaItemRequest request) {
    VentaItem item = new VentaItem();
    item.setProductId(request.getProductId());
    item.setVarianteId(request.getVarianteId());
    item.setQuantity(request.getQuantity());
    item.setSize(request.getSize());
    item.setColor(request.getColor());
    return item;
  }

  private VentaItemResponse toItemResponse(VentaItem item) {
    VentaItemResponse response = new VentaItemResponse();
    response.setId(item.getId());
    response.setProductId(item.getProductId());
    response.setVarianteId(item.getVarianteId());
    response.setProductName(item.getProductName());
    response.setQuantity(item.getQuantity());
    response.setSize(item.getSize());
    response.setColor(item.getColor());
    response.setUnitPrice(item.getUnitPrice());
    response.setSubtotal(item.getSubtotal());
    return response;
  }
}
