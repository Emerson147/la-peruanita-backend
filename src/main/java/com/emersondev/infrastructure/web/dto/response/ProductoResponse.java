package com.emersondev.infrastructure.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponse {

  private UUID id;
  private String name;
  private String category;
  private String brand;
  private BigDecimal price;
  private BigDecimal cost;
  private Integer stock;
  private Integer minStock;
  private List<String> sizes;
  private List<String> colors;
  private String image;
  private String barcode;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // Campos calculados - vienen de las reglas de negocio
  private boolean stockBajo;
  private BigDecimal ganancia;

  private List<VarianteResponse> variants;
}
