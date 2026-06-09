package com.emersondev.infrastructure.web.dto.response;

import java.util.UUID;

public class VarianteResponse {

  private UUID id;
  private UUID productId;
  private String size;
  private String color;
  private Integer stock;
  private String barcode;
  private boolean tieneStock;
  private UUID almacenId;
  private String almacenName;

  public VarianteResponse() {}

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }

  public UUID getProductId() { return productId; }
  public void setProductId(UUID productId) { this.productId = productId; }

  public String getSize() { return size; }
  public void setSize(String size) { this.size = size; }

  public String getColor() { return color; }
  public void setColor(String color) { this.color = color; }

  public Integer getStock() { return stock; }
  public void setStock(Integer stock) { this.stock = stock; }

  public String getBarcode() { return barcode; }
  public void setBarcode(String barcode) { this.barcode = barcode; }

  public boolean isTieneStock() { return tieneStock; }
  public void setTieneStock(boolean tieneStock) { this.tieneStock = tieneStock; }

  public UUID getAlmacenId() { return almacenId; }
  public void setAlmacenId(UUID almacenId) { this.almacenId = almacenId; }

  public String getAlmacenName() { return almacenName; }
  public void setAlmacenName(String almacenName) { this.almacenName = almacenName; }
}