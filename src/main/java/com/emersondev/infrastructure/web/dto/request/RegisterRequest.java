package com.emersondev.infrastructure.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

  @NotBlank
  private String nombre;

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String password;

  private String rol; // Admin o vendedor
}
