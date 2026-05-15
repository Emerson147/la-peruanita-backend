package com.emersondev.infrastructure.web.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class MovimientoResponse {

  private UUID id;
  private String movementNumber;
  private String type;
  private UUID productId;
  private UUID varianteId;
  private String productName;
  private Integer quantity;
  private Integer quantityBefore;
  private Integer quantityAfter;
  private String reason;
  private String supplier;
  private String invoice;
  private BigDecimal unitCost;
  private BigDecimal totalCost;
  private String createdBy;
  private String notes;
  private LocalDateTime createdAt;

  public MovimientoResponse() {}

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }

  public String getMovementNumber() { return movementNumber; }
  public void setMovementNumber(String m) { this.movementNumber = m; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public UUID getProductId() { return productId; }
  public void setProductId(UUID p) { this.productId = p; }

  public UUID getVarianteId() { return varianteId; }
  public void setVarianteId(UUID v) { this.varianteId = v; }

  public String getProductName() { return productName; }
  public void setProductName(String p) { this.productName = p; }

  public Integer getQuantity() { return quantity; }
  public void setQuantity(Integer q) { this.quantity = q; }

  public Integer getQuantityBefore() { return quantityBefore; }
  public void setQuantityBefore(Integer q) { this.quantityBefore = q; }

  public Integer getQuantityAfter() { return quantityAfter; }
  public void setQuantityAfter(Integer q) { this.quantityAfter = q; }

  public String getReason() { return reason; }
  public void setReason(String reason) { this.reason = reason; }

  public String getSupplier() { return supplier; }
  public void setSupplier(String s) { this.supplier = s; }

  public String getInvoice() { return invoice; }
  public void setInvoice(String i) { this.invoice = i; }

  public BigDecimal getUnitCost() { return unitCost; }
  public void setUnitCost(BigDecimal u) { this.unitCost = u; }

  public BigDecimal getTotalCost() { return totalCost; }
  public void setTotalCost(BigDecimal t) { this.totalCost = t; }

  public String getCreatedBy() { return createdBy; }
  public void setCreatedBy(String c) { this.createdBy = c; }

  public String getNotes() { return notes; }
  public void setNotes(String notes) { this.notes = notes; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
}