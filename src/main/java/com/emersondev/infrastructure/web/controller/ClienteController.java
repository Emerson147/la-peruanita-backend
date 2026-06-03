package com.emersondev.infrastructure.web.controller;

import com.emersondev.application.usecase.cliente.ActualizarClienteUseCase;
import com.emersondev.application.usecase.cliente.CrearClienteUseCase;
import com.emersondev.application.usecase.cliente.ObtenerClientesUseCase;
import com.emersondev.domain.exception.ClienteNotFoundException;
import com.emersondev.domain.model.Cliente;
import com.emersondev.infrastructure.web.dto.request.ClienteRequest;
import com.emersondev.infrastructure.web.dto.response.ClienteResponse;
import com.emersondev.infrastructure.web.mapper.ClienteDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

  private final CrearClienteUseCase crearClienteUseCase;
  private final ObtenerClientesUseCase obtenerClientesUseCase;
  private final ActualizarClienteUseCase actualizarClienteUseCase;
  private final ClienteDtoMapper mapper;

  // POST /api/clientes
  @PostMapping
  public ResponseEntity<ClienteResponse> crear(
          @Valid @RequestBody ClienteRequest request) {
    Cliente cliente = crearClienteUseCase
            .ejecutar(mapper.toDomain(request));
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(mapper.toResponse(cliente));
  }

  // GET /api/clientes
  @GetMapping
  public ResponseEntity<Page<ClienteResponse>> obtenerTodos(
          @RequestParam(required = false) String tier,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "createdAt") String sortBy,
          @RequestParam(defaultValue = "desc") String sortDir) {

    Pageable pageable = PageRequest.of(page, size,
            sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending());

    return ResponseEntity.ok(obtenerClientesUseCase
            .obtenerTodos(tier, pageable)
            .map(mapper::toResponse));
  }

  // GET /api/clientes/{id}
  @GetMapping("/{id}")
  public ResponseEntity<ClienteResponse> obtenerPorId(
          @PathVariable UUID id) {
    Cliente cliente = obtenerClientesUseCase
            .obtenerPorId(id)
            .orElseThrow(() ->
                    new ClienteNotFoundException(id.toString()));
    return ResponseEntity.ok(mapper.toResponse(cliente));
  }

  // GET /api/clientes/telefono/{phone}
  @GetMapping("/telefono/{phone}")
  public ResponseEntity<ClienteResponse> obtenerPorTelefono(
          @PathVariable String phone) {
    Cliente cliente = obtenerClientesUseCase
            .obtenerPorTelefono(phone)
            .orElseThrow(() ->
                    new ClienteNotFoundException(phone));
    return ResponseEntity.ok(mapper.toResponse(cliente));
  }

  // PUT /api/clientes/{id}
  @PutMapping("/{id}")
  public ResponseEntity<ClienteResponse> actualizar(
          @PathVariable UUID id,
          @Valid @RequestBody ClienteRequest request) {
    Cliente actualizado = actualizarClienteUseCase
            .ejecutar(id, mapper.toDomain(request));
    return ResponseEntity.ok(mapper.toResponse(actualizado));
  }
}