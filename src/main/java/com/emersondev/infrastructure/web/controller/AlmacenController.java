package com.emersondev.infrastructure.web.controller;

import com.emersondev.application.usecase.almacen.ActualizarAlmacenUseCase;
import com.emersondev.application.usecase.almacen.CrearAlmacenUseCase;
import com.emersondev.application.usecase.almacen.EliminarAlmacenUseCase;
import com.emersondev.application.usecase.almacen.ObtenerAlmacenesUseCase;
import com.emersondev.domain.model.Almacen;
import com.emersondev.infrastructure.web.dto.request.AlmacenRequest;
import com.emersondev.infrastructure.web.dto.response.AlmacenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/almacenes")
@RequiredArgsConstructor
public class AlmacenController {

  private final CrearAlmacenUseCase crearAlmacenUseCase;
  private final ObtenerAlmacenesUseCase obtenerAlmacenesUseCase;
  private final ActualizarAlmacenUseCase actualizarAlmacenUseCase;
  private final EliminarAlmacenUseCase eliminarAlmacenUseCase;

  @PostMapping
  public ResponseEntity<AlmacenResponse> crear(@Valid @RequestBody AlmacenRequest request) {
    Almacen almacen = new Almacen();
    almacen.setNombre(request.getNombre());
    almacen.setDireccion(request.getDireccion());
    if (request.getActivo() != null) {
      almacen.setActivo(request.getActivo());
    }
    Almacen creado = crearAlmacenUseCase.ejecutar(almacen);
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creado));
  }

  @GetMapping
  public ResponseEntity<List<AlmacenResponse>> listar() {
    List<AlmacenResponse> response = obtenerAlmacenesUseCase.obtenerTodos().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AlmacenResponse> obtenerPorId(@PathVariable UUID id) {
    Almacen almacen = obtenerAlmacenesUseCase.obtenerPorId(id);
    return ResponseEntity.ok(toResponse(almacen));
  }

  @PutMapping("/{id}")
  public ResponseEntity<AlmacenResponse> actualizar(@PathVariable UUID id, @Valid @RequestBody AlmacenRequest request) {
    Almacen almacenActualizado = new Almacen();
    almacenActualizado.setNombre(request.getNombre());
    almacenActualizado.setDireccion(request.getDireccion());
    if (request.getActivo() != null) {
      almacenActualizado.setActivo(request.getActivo());
    } else {
      almacenActualizado.setActivo(true);
    }
    Almacen guardado = actualizarAlmacenUseCase.ejecutar(id, almacenActualizado);
    return ResponseEntity.ok(toResponse(guardado));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
    eliminarAlmacenUseCase.ejecutar(id);
    return ResponseEntity.noContent().build();
  }
  private AlmacenResponse toResponse(Almacen almacen) {
    return AlmacenResponse.builder()
            .id(almacen.getId())
            .nombre(almacen.getNombre())
            .direccion(almacen.getDireccion())
            .activo(almacen.isActivo())
            .build();
  }
}
