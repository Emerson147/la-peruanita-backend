package com.emersondev.infrastructure.web.mapper;

import com.emersondev.domain.model.Producto;
import com.emersondev.domain.model.Variante;
import com.emersondev.infrastructure.web.dto.request.ProductoRequest;
import com.emersondev.infrastructure.web.dto.request.VarianteRequest;
import com.emersondev.infrastructure.web.dto.response.ProductoResponse;
import com.emersondev.infrastructure.web.dto.response.VarianteResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductoDtoMapper {

  public Producto toDomain(ProductoRequest request) {
    return Producto.builder()
            .name(request.getName())
            .categoria(request.getCategory())
            .brand(request.getBrand())
            .price(request.getPrice())
            .cost(request.getCost())
            .sizes(request.getSizes())
            .colors(request.getColors())
            .image(request.getImage())
            .barcode(request.getBarcode())
            .status(request.getStatus())
            .build();
  }

  public List<Variante> toVariantesDomain(
          List<VarianteRequest> requests) {
    if (requests == null) return List.of();
    return requests.stream()
            .map(this::toVarianteDomain)
            .toList();
  }

  public ProductoResponse toResponse(Producto producto) {
    ProductoResponse response = new ProductoResponse();
    response.setId(producto.getId());
    response.setName(producto.getName());
    response.setCategory(producto.getCategoria());
    response.setBrand(producto.getBrand());
    response.setPrice(producto.getPrice());
    response.setCost(producto.getCost());
    response.setStock(producto.getStockTotal());
    response.setMinStock(producto.getMinStockTotal());
    response.setSizes(producto.getSizes());
    response.setColors(producto.getColors());
    response.setImage(producto.getImage());
    response.setBarcode(producto.getBarcode());
    response.setStatus(producto.getStatus());
    response.setCreatedAt(producto.getCreatedAt());
    response.setUpdatedAt(producto.getUpdatedAt());
    response.setGanancia(producto.calcularGanancia());

    if (producto.getVariantes() != null) {
      response.setVariants(producto.getVariantes().stream()
              .map(this::toVarianteResponse)
              .toList());
    }

    return response;
  }

  public VarianteResponse toVarianteResponse(Variante variante) {
    VarianteResponse response = new VarianteResponse();
    response.setId(variante.getId());
    response.setProductId(variante.getProductId());
    response.setSize(variante.getSize());
    response.setColor(variante.getColor());
    response.setStock(variante.getStockTotal());
    response.setBarcode(variante.getBarcode());
    response.setTieneStock(variante.tieneStock());
    return response;
  }

  private Variante toVarianteDomain(VarianteRequest request) {
    Variante variante = new Variante();
    variante.setSize(request.getSize());
    variante.setColor(request.getColor());
    variante.setBarcode(request.getBarcode());

    if (request.getStock() != null) {
      com.emersondev.domain.model.Inventario inv = new com.emersondev.domain.model.Inventario();
      inv.setStock(request.getStock());
      inv.setAlmacenId(request.getAlmacenId());
      inv.setNombreAlmacen(request.getNombreAlmacen());
      variante.setInventarios(java.util.List.of(inv));
    }
    return variante;
  }
}