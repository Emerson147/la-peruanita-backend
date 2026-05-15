package com.emersondev.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class UsuarioEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String nombre;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String rol;

  @Column(nullable = false)
  private boolean activo = true;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
