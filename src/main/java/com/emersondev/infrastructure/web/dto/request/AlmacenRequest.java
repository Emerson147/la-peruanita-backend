package com.emersondev.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlmacenRequest {

  @NotBlank(message = "El nombre es obligatorio")
  private String nombre;
  private String direccion;
  private Boolean activo;
}
