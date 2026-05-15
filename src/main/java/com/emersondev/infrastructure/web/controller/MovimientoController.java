package com.emersondev.infrastructure.web.controller;

import com.emersondev.application.usecase.movimiento.ObtenerMovimientosUseCase;
import com.emersondev.application.usecase.movimiento.RegistrarMovimientoUseCase;
import com.emersondev.domain.model.MovimientoInventario;
import com.emersondev.infrastructure.web.dto.request.MovimientoRequest;
import com.emersondev.infrastructure.web.dto.response.MovimientoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

  private final RegistrarMovimientoUseCase registrarUseCase;
  private final ObtenerMovimientosUseCase obtenerUseCase;

  // POST /api/movimientos
  @PostMapping
  public ResponseEntity<MovimientoResponse> registrar(
          @Valid @RequestBody MovimientoRequest request) {

    MovimientoInventario movimiento = toDomain(request);
    MovimientoInventario registrado =
            registrarUseCase.ejecutar(movimiento);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(toResponse(registrado));
  }

  // GET /api/movimientos
  @GetMapping
  public ResponseEntity<Page<MovimientoResponse>> obtenerTodos(
          @RequestParam(required = false) String type,
          @RequestParam(required = false) UUID productId,
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

    Page<MovimientoInventario> movimientos;

    if (type != null) {
      movimientos = obtenerUseCase.obtenerPorTipoPaginado(type, pageable);
    } else {
      movimientos = obtenerUseCase.obtenerTodosPaginado(pageable);
    }

    return ResponseEntity.ok(movimientos.map(this::toResponse));
  }

  // Mappers inline — sin clase separada por simplicidad
  private MovimientoInventario toDomain(
          MovimientoRequest request) {
    MovimientoInventario m = new MovimientoInventario();
    m.setType(request.getType());
    m.setProductId(request.getProductId());
    m.setVarianteId(request.getVarianteId());
    m.setQuantity(request.getQuantity());
    m.setReason(request.getReason());
    m.setSupplier(request.getSupplier());
    m.setInvoice(request.getInvoice());
    m.setUnitCost(request.getUnitCost());
    m.setCreatedBy(request.getCreatedBy());
    m.setNotes(request.getNotes());
    return m;
  }

  private MovimientoResponse toResponse(
          MovimientoInventario m) {
    MovimientoResponse r = new MovimientoResponse();
    r.setId(m.getId());
    r.setMovementNumber(m.getMovementNumber());
    r.setType(m.getType());
    r.setProductId(m.getProductId());
    r.setVarianteId(m.getVarianteId());
    r.setProductName(m.getProductName());
    r.setQuantity(m.getQuantity());
    r.setQuantityBefore(m.getQuantityBefore());
    r.setQuantityAfter(m.getQuantityAfter());
    r.setReason(m.getReason());
    r.setSupplier(m.getSupplier());
    r.setInvoice(m.getInvoice());
    r.setUnitCost(m.getUnitCost());
    r.setTotalCost(m.getTotalCost());
    r.setCreatedBy(m.getCreatedBy());
    r.setNotes(m.getNotes());
    r.setCreatedAt(m.getCreatedAt());
    return r;
  }
}