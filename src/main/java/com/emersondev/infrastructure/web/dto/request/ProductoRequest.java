package com.emersondev.infrastructure.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequest {

  @NotBlank(message = "El nombre es obligatorio")
  private String name;

  private String category;
  private String brand;

  @NotNull(message = "El precio es obligatorio")
  @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
  private BigDecimal price;

  private BigDecimal cost;
  private Integer stock;
  private Integer minStock;
  private List<String> sizes;
  private List<String> colors;
  private String image;
  private String barcode;
  private String status;

  // ← variantes incluidas en el mismo request
  @Valid
  private List<VarianteRequest> variants;
}
