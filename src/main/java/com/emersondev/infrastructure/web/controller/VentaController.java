package com.emersondev.infrastructure.web.controller;

import com.emersondev.application.usecase.venta.CancelarVentaUseCase;
import com.emersondev.application.usecase.venta.ObtenerVentasUseCase;
import com.emersondev.application.usecase.venta.RegistrarVentaUseCase;
import com.emersondev.domain.model.Venta;
import com.emersondev.infrastructure.web.dto.request.VentaRequest;
import com.emersondev.infrastructure.web.dto.response.VentaResponse;
import com.emersondev.infrastructure.web.mapper.VentaDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import com.emersondev.infrastructure.persistence.repository.UsuarioRepositoryAdapter;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

  private final RegistrarVentaUseCase registrarVentaUseCase;
  private final ObtenerVentasUseCase obtenerVentasUseCase;
  private final CancelarVentaUseCase cancelarVentaUseCase;
  private final VentaDtoMapper mapper;
  private final UsuarioRepositoryAdapter usuarioRepository;

  // POST /api/ventas
  @PostMapping
  public ResponseEntity<VentaResponse> registrar(
          @Valid @RequestBody VentaRequest request,
          @AuthenticationPrincipal UserDetails userDetails) {

    Venta venta = mapper.toDomain(request);
    
    // 🔥 FORZAR Vendedor ID desde la BD real de Java (previene falsificaciones y conflictos de frontend)
    if (userDetails != null) {
        usuarioRepository.findByEmail(userDetails.getUsername())
             .ifPresent(usuario -> venta.setVendedorId(usuario.getId()));
    }

    Venta registrada = registrarVentaUseCase.ejecutar(venta);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(mapper.toResponse(registrada));
  }

  // GET /api/ventas
  @GetMapping
  public ResponseEntity<Page<VentaResponse>> obtenerTodas(
          @RequestParam(required = false) String status,
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime desde,
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime hasta,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "createdAt") String sortBy,
          @RequestParam(defaultValue = "desc") String sortDir) {

    Pageable pageable = PageRequest.of(page, size,
            sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending());

    return ResponseEntity.ok(
            obtenerVentasUseCase
                    . obtenerTodos(status, desde, hasta, pageable)
                    .map(mapper::toResponse));
  }

  // GET /api/ventas/{id}
  @GetMapping("/{id}")
  public ResponseEntity<VentaResponse> obtenerPorId(@PathVariable UUID id) {
    Venta venta = obtenerVentasUseCase.obtenerPorId(id);
    return ResponseEntity.ok(mapper.toResponse(venta));
  }

  // GET /api/ventas/numero/{saleNumber}
  @GetMapping("/numero/{saleNumber}")
  public ResponseEntity<VentaResponse> obtenerPorNumero(
          @PathVariable String saleNumber) {
    Venta venta = obtenerVentasUseCase.obtenerPorNumero(saleNumber);
    return ResponseEntity.ok(mapper.toResponse(venta));
  }

  // POST /api/ventas/{id}/cancelar
  @PostMapping("/{id}/cancelar")
  public ResponseEntity<VentaResponse> cancelar(@PathVariable UUID id) {
    Venta cancelada = cancelarVentaUseCase.ejecutar(id);
    return ResponseEntity.ok(mapper.toResponse(cancelada));
  }
}
