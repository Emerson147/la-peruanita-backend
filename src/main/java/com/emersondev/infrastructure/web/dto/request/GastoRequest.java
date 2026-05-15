package com.emersondev.infrastructure.web.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class GastoRequest {

  @NotBlank(message = "La descripción es obligatoria")
  private String description;

  @NotNull(message = "El monto es obligatorio")
  @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
  private BigDecimal amount;

  private String category; // rent, utilities, supplies, salary, other
  private String receipt;
  private UUID userId;
}