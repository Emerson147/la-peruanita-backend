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
public class Usuario {

  private UUID id;
  private String nombre;
  private String email;
  private String password;
  private String rol; // ADMIN o VENDOR
  private boolean activo;
  private LocalDateTime createdAt;

  // Regla de negocio
  public boolean esAdmin() {
    return "ADMIN".equals(this.rol);
  }

  public boolean esVendedor() {
    return "VENDOR".equals(this.rol);
  }


}
