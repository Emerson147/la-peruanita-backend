package com.emersondev.infrastructure.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class MovimientoRequest {

  @NotBlank(message = "El tipo es obligatorio")
  private String type; // entrada | ajuste

  @NotNull(message = "El producto es obligatorio")
  private UUID productId;

  private UUID varianteId;

  @NotNull(message = "La cantidad es obligatoria")
  private Integer quantity;

  @NotBlank(message = "El motivo es obligatorio")
  private String reason;

  private String supplier;
  private String invoice;
  private BigDecimal unitCost;
  private String createdBy;
  private String notes;
}