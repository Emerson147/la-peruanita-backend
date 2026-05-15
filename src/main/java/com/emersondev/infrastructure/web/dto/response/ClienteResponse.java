package com.emersondev.infrastructure.web.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ClienteResponse {

  private UUID id;
  private String name;
  private String phone;
  private String email;
  private String address;
  private BigDecimal totalPurchases;
  private LocalDateTime lastPurchaseDate;
  private String tier;
  private List<String> preferences;
  private LocalDateTime createdAt;

  public ClienteResponse() {}

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }

  public BigDecimal getTotalPurchases() { return totalPurchases; }
  public void setTotalPurchases(BigDecimal t) { this.totalPurchases = t; }

  public LocalDateTime getLastPurchaseDate() { return lastPurchaseDate; }
  public void setLastPurchaseDate(LocalDateTime l) {
    this.lastPurchaseDate = l; }

  public String getTier() { return tier; }
  public void setTier(String tier) { this.tier = tier; }

  public List<String> getPreferences() { return preferences; }
  public void setPreferences(List<String> p) { this.preferences = p; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
}