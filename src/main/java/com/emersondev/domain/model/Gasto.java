package com.emersondev.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gasto {

  private UUID id;
  private String description;
  private BigDecimal amount;
  private String category; // rent, utilities, supplies, salary, other
  private String receipt;
  private UUID userId;
  private LocalDateTime createdAt;

  // Regla de negocio — nombre legible de categoría
  public String getCategoryLabel() {
    return switch (this.category) {
      case "rent"      -> "Alquiler";
      case "utilities" -> "Servicios";
      case "supplies"  -> "Insumos";
      case "salary"    -> "Salario";
      default          -> "Otros";
    };
  }

}