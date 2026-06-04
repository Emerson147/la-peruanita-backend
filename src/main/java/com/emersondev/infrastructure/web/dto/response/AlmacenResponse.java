package com.emersondev.infrastructure.web.dto.response;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
@Getter
@Setter
@Builder
public class AlmacenResponse {
  private UUID id;
  private String nombre;
  private String direccion;
  private boolean activo;
}
