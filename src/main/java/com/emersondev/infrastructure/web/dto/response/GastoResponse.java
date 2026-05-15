package com.emersondev.infrastructure.web.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class GastoResponse {

  private UUID id;
  private String description;
  private BigDecimal amount;
  private String category;
  private String categoryLabel;
  private String receipt;
  private UUID userId;
  private LocalDateTime createdAt;

  public GastoResponse() {}

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }

  public String getDescription() { return description; }
  public void setDescription(String d) { this.description = d; }

  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }

  public String getCategory() { return category; }
  public void setCategory(String category) { this.category = category; }

  public String getCategoryLabel() { return categoryLabel; }
  public void setCategoryLabel(String c) { this.categoryLabel = c; }

  public String getReceipt() { return receipt; }
  public void setReceipt(String receipt) { this.receipt = receipt; }

  public UUID getUserId() { return userId; }
  public void setUserId(UUID userId) { this.userId = userId; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
}