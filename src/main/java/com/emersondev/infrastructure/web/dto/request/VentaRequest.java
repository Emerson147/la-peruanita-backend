package com.emersondev.infrastructure.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequest {

  private UUID userId;
  private UUID vendedorId;
  private UUID customerId;

  @NotNull(message = "El almacén es obligatorio")
  private UUID almacenId;

  @NotEmpty(message = "La venta debe tener al menos un item")
  @Valid
  private List<VentaItemRequest> items;

  private BigDecimal discount;
  private BigDecimal tax;

  @NotNull(message = "El método de pago es obligatorio")
  private String paymentMethod;

  private String notes;
  private String createdBy;
}
