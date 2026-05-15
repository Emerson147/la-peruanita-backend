package com.emersondev.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClienteRequest {

  @NotBlank(message = "El nombre es obligatorio")
  private String name;

  private String phone;
  private String email;
  private String address;
  private List<String> preferences;
}