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
public class UserStats {

  private UUID id;
  private UUID userId;
  private Integer totalPoints;
  private Integer level;
  private Integer currentStreak;
  private Integer longestStreak;
  private Integer totalSalesCompleted;
  private BigDecimal totalRevenueGenerated;
  private LocalDateTime joinedAt;

  // Regla de negocio — calcular nivel
  public void recalcularNivel() {
    this.level = (this.totalPoints / 500) + 1;
  }

  // Regla de negocio — agregar puntos
  public void agregarPuntos(int puntos) {
    this.totalPoints = (this.totalPoints != null
            ? this.totalPoints : 0) + puntos;
    recalcularNivel();
  }

  // Regla de negocio — puntos para siguiente nivel
  public int puntosParaSiguienteNivel() {
    return (this.level * 500) - this.totalPoints;
  }

}