package com.emersondev.infrastructure.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaItemRequest {

  @NotNull(message = "El producto es obligatorio")
  private UUID productId;

  private UUID varianteId;

  @NotNull(message = "La cantidad es obligatoria")
  @Min(value = 1, message = "La cantidad mínima es 1")
  private Integer quantity;

  private String size;
  private String color;
}
