package com.emersondev.infrastructure.web.controller;

import com.emersondev.application.usecase.gasto.EliminarGastoUseCase;
import com.emersondev.application.usecase.gasto.ObtenerGastosUseCase;
import com.emersondev.application.usecase.gasto.RegistrarGastoUseCase;
import com.emersondev.domain.model.Gasto;
import com.emersondev.infrastructure.web.dto.request.GastoRequest;
import com.emersondev.infrastructure.web.dto.response.GastoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/gastos")
@RequiredArgsConstructor
public class GastoController {

  private final RegistrarGastoUseCase registrarGastoUseCase;
  private final ObtenerGastosUseCase obtenerGastosUseCase;
  private final EliminarGastoUseCase eliminarGastoUseCase;

  // POST /api/gastos
  @PostMapping
  public ResponseEntity<GastoResponse> registrar(
          @Valid @RequestBody GastoRequest request,
          @AuthenticationPrincipal UserDetails userDetails) {

    Gasto gasto = toDomain(request);

    //Extraer usuario del token JWT
    // userDetails.getUsername() -> devuelve el email
    log.info("Gasto registrado por: {}", userDetails.getUsername());

    Gasto registrado =  registrarGastoUseCase.ejecutar(gasto);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(toResponse(registrado));
  }

  // GET /api/gastos
  @GetMapping
  public ResponseEntity<List<GastoResponse>> obtenerTodos(
          @RequestParam(required = false) String category,
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime desde,
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime hasta) {

    List<Gasto> gastos;

    if (category != null) {
      gastos = obtenerGastosUseCase
              .obtenerPorCategoria(category);
    } else if (desde != null && hasta != null) {
      gastos = obtenerGastosUseCase
              .obtenerPorFecha(desde, hasta);
    } else {
      gastos = obtenerGastosUseCase.obtenerTodos();
    }

    return ResponseEntity.ok(gastos.stream()
            .map(this::toResponse).toList());
  }

  // GET /api/gastos/{id}
  @GetMapping("/{id}")
  public ResponseEntity<GastoResponse> obtenerPorId(
          @PathVariable UUID id) {
    return ResponseEntity.ok(
            toResponse(obtenerGastosUseCase.obtenerPorId(id)));
  }

  // DELETE /api/gastos/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
    eliminarGastoUseCase.ejecutar(id);
    return ResponseEntity.noContent().build();
  }

  private Gasto toDomain(GastoRequest request) {
    Gasto gasto = new Gasto();
    gasto.setDescription(request.getDescription());
    gasto.setAmount(request.getAmount());
    gasto.setCategory(request.getCategory());
    gasto.setReceipt(request.getReceipt());
    gasto.setUserId(request.getUserId());
    return gasto;
  }

  private GastoResponse toResponse(Gasto gasto) {
    GastoResponse response = new GastoResponse();
    response.setId(gasto.getId());
    response.setDescription(gasto.getDescription());
    response.setAmount(gasto.getAmount());
    response.setCategory(gasto.getCategory());
    response.setCategoryLabel(gasto.getCategoryLabel());
    response.setReceipt(gasto.getReceipt());
    response.setUserId(gasto.getUserId());
    response.setCreatedAt(gasto.getCreatedAt());
    return response;
  }
}