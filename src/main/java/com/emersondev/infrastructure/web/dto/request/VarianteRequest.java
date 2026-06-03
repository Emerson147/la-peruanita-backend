package com.emersondev.infrastructure.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VarianteRequest {

  @NotBlank(message = "La talla es obligatoria")
  private String size;

  private String color;

  @Min(value = 0, message = "El stock no puede ser negativo")
  private Integer stock;

  private String barcode;

  private java.util.UUID almacenId;

  private String nombreAlmacen;
}