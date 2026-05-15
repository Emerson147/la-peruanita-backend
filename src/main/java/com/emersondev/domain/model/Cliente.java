package com.emersondev.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

  private UUID id;
  private String name;
  private String phone;
  private String email;
  private String address;
  private BigDecimal totalPurchases;
  private LocalDateTime lastPurchaseDate;
  private String tier; // nuevo, silver, gold
  private List<String> preferences;
  private LocalDateTime createdAt;

  // Reglas de negocio — actualizar tier automáticamente
  public void actualizarTier() {
    if (this.totalPurchases == null) {
      this.tier = "nuevo";
      return;
    }
    if (this.totalPurchases.compareTo(new BigDecimal("1000")) >= 0) {
      this.tier = "gold";
    } else if (this.totalPurchases.compareTo(new BigDecimal("300")) >= 0) {
      this.tier = "silver";
    } else {
      this.tier = "nuevo";
    }
  }

  // Regla de negocio — registrar una compra
  public void registrarCompra(BigDecimal monto) {
    if (this.totalPurchases == null) {
      this.totalPurchases = BigDecimal.ZERO;
    }
    this.totalPurchases = this.totalPurchases.add(monto);
    this.lastPurchaseDate = LocalDateTime.now();
    this.actualizarTier();
  }

  // Regla de negocio — actualizar perfil con datos nuevos (solo si no son nulos)
  public void actualizarPerfil(Cliente datosNuevos) {
    if (datosNuevos.getName() != null) this.name = datosNuevos.getName();
    if (datosNuevos.getPhone() != null) this.phone = datosNuevos.getPhone();
    if (datosNuevos.getEmail() != null) this.email = datosNuevos.getEmail();
    if (datosNuevos.getAddress() != null) this.address = datosNuevos.getAddress();
    if (datosNuevos.getPreferences() != null) this.preferences = datosNuevos.getPreferences();
  }

  public boolean esGold() { return "gold".equals(this.tier); }
  public boolean esSilver() { return "silver".equals(this.tier); }

}