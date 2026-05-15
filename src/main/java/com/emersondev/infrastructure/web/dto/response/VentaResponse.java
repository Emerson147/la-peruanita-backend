package com.emersondev.infrastructure.web.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VentaResponse {

  private UUID id;
  private String saleNumber;
  private UUID userId;
  private UUID vendedorId;
  private UUID customerId;
  private List<VentaItemResponse> items;
  private BigDecimal subtotal;
  private BigDecimal discount;
  private BigDecimal tax;
  private BigDecimal total;
  private String paymentMethod;
  private String status;
  private String notes;
  private String createdBy;
  private LocalDateTime createdAt;

  // Campo calculado
  private Integer totalItems;
}
