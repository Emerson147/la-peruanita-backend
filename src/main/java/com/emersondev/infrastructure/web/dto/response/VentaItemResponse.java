package com.emersondev.infrastructure.web.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaItemResponse {

  private UUID id;
  private UUID productId;
  private String productName;
  private Integer quantity;
  private String size;
  private String color;
  private BigDecimal unitPrice;
  private BigDecimal subtotal;
}
