package com.emersondev.infrastructure.web.dto.response;

public class AuthResponse {

  private String token;
  private String id;
  private String email;
  private String nombre;
  private String rol;
  private long expiresIn;

  public AuthResponse() {
  }

  public AuthResponse(String token, String id, String email, String nombre, String rol, long expiresIn) {
    this.token = token;
    this.id = id;
    this.email = email;
    this.nombre = nombre;
    this.rol = rol;
    this.expiresIn = expiresIn;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getRol() {
    return rol;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  public long getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(long expiresIn) {
    this.expiresIn = expiresIn;
  }
}
