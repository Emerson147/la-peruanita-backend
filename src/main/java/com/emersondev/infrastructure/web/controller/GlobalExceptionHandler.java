package com.emersondev.infrastructure.web.controller;

import com.emersondev.domain.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // Error 404 - no encontrado
  @ExceptionHandler(ProductoNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleProductoNotFound(
          ProductoNotFoundException ex) {

    log.error("Recurso no encontrado: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(buildError(404, ex.getMessage()));
  }

  // Error 400 - stock insuficiente
  @ExceptionHandler(StockInsuficienteException.class)
  public ResponseEntity<Map<String, Object>> handleStockInsuficiente(
          StockInsuficienteException ex) {

    log.error("Stock insuficiente: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(buildError(400, ex.getMessage()));
  }

  // Error 400 - validaciones fallidas (@NotBlank, @NotNull, etc)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidacion(
          MethodArgumentNotValidException ex) {

    Map<String, String> errores = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
            .forEach(error -> errores.put(
                    error.getField(),
                    error.getDefaultMessage()));

    log.error("Error de validación: {}", errores);

    Map<String, Object> response = buildError(400, "Error de validación");
    response.put("errores", errores);

    return ResponseEntity.badRequest().body(response);
  }

  // Error 500 - cualquier otro error inesperado
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
    log.error("Error inesperado: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(buildError(500, "Error interno del servidor"));
  }

  private Map<String, Object> buildError(int status, String message) {
    Map<String, Object> error = new HashMap<>();
    error.put("status", status);
    error.put("message", message);
    error.put("timestamp", LocalDateTime.now());
    return error;
  }

  @ExceptionHandler(VentaNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleVentaNotFound(
          VentaNotFoundException ex) {
    log.error("Venta no encontrada: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(buildError(404, ex.getMessage()));
  }

  @ExceptionHandler(EmailYaRegistradoException.class)
  public ResponseEntity<Map<String, Object>> handleEmailYaRegistrado(
          EmailYaRegistradoException ex) {
    log.warn("Conflicto de email: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(buildError(409, ex.getMessage()));
  }

  @ExceptionHandler(ClienteNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleClienteNotFound(
          ClienteNotFoundException ex) {
    log.warn("Cliente no encontrado: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(buildError(404, ex.getMessage()));
  }

  @ExceptionHandler(GastoNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleGastoNotFound(
          GastoNotFoundException ex) {
    log.warn("Gasto no encontrado: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(buildError(404, ex.getMessage()));
  }

}
