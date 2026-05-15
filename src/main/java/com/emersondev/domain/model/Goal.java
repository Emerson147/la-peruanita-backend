package com.emersondev.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goal {

  private UUID id;
  private UUID userId;
  private String title;
  private String description;
  private String type;   // daily, weekly, monthly, custom
  private String metric; // sales_count, revenue, new_customers, avg_ticket
  private Integer target;
  private Integer current;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private String status; // active, completed, failed, cancelled
  private String reward;

  // Regla de negocio — calcular porcentaje de progreso
  public double calcularProgreso() {
    if (this.target == null || this.target == 0) return 0;
    return Math.min(
            ((double) this.current / this.target) * 100, 100);
  }

  // Regla de negocio — verificar si completó
  public boolean estaCompleta() {
    return this.current != null &&
            this.target != null &&
            this.current >= this.target;
  }

}