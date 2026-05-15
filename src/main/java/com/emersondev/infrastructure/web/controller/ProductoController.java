package com.emersondev.infrastructure.web.controller;

import com.emersondev.application.usecase.producto.ActualizarProductoUseCase;
import com.emersondev.application.usecase.producto.CrearProductoUseCase;
import com.emersondev.application.usecase.producto.EliminarProductoUseCase;
import com.emersondev.application.usecase.producto.ObtenerProductosUseCase;
import com.emersondev.domain.exception.ProductoNotFoundException;
import com.emersondev.domain.model.Producto;
import com.emersondev.domain.model.Variante;
import com.emersondev.domain.repository.VarianteRepository;
import com.emersondev.infrastructure.web.dto.request.ProductoRequest;
import com.emersondev.infrastructure.web.dto.response.ProductoResponse;
import com.emersondev.infrastructure.web.dto.response.VarianteResponse;
import com.emersondev.infrastructure.web.mapper.ProductoDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

  private final CrearProductoUseCase crearProductoUseCase;
  private final ObtenerProductosUseCase obtenerProductosUseCase;
  private final ActualizarProductoUseCase actualizarProductoUseCase;
  private final EliminarProductoUseCase eliminarProductoUseCase;
  private final VarianteRepository varianteRepository;
  private final ProductoDtoMapper mapper;

  // POST /api/productos
  @PostMapping
  public ResponseEntity<ProductoResponse> crear(
          @Valid @RequestBody ProductoRequest request) {

    Producto creado = crearProductoUseCase.ejecutar(
            mapper.toDomain(request),
            mapper.toVariantesDomain(request.getVariants()));

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(mapper.toResponse(
                    obtenerProductosUseCase
                            .obtenerPorId(creado.getId())
            ));
  }

  // GET /api/productos
  @GetMapping
  public ResponseEntity<Page<ProductoResponse>> obtenerTodos(
          @RequestParam(required = false) String category,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "createdAt") String sortBy,
          @RequestParam(defaultValue = "desc") String sortDir)  {

    Pageable pageable = PageRequest.of(page, size, sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending());

    return ResponseEntity.ok(
            obtenerProductosUseCase
                    .obtenerTodos(category, pageable)
                    .map(mapper::toResponse));

  }

  // GET /api/productos/{id}
  @GetMapping("/{id}")
  public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable UUID id) {

   return ResponseEntity.ok(
           mapper.toResponse(obtenerProductosUseCase.obtenerPorId(id)));
  }

  // GET /api/productos/{id}/variantes
  @GetMapping("/{id}/variantes")
  public ResponseEntity<List<VarianteResponse>> obtenerVariantes(
          @PathVariable UUID id) {
    return ResponseEntity.ok(
            obtenerProductosUseCase.obtenerVariantes(id)
                    .stream()
                    .map(mapper::toVarianteResponse)
                    .toList());
  }

  // GET /api/productos/stock-bajo
  @GetMapping("/stock-bajo")
  public ResponseEntity<List<ProductoResponse>> obtenerStockBajo() {
    return ResponseEntity.ok(
            obtenerProductosUseCase.obtenerConStockBajo()
                    .stream()
                    .map(mapper::toResponse)
                    .toList());
  }

  // PUT /api/productos/{id}
  @PutMapping("/{id}")
  public ResponseEntity<ProductoResponse> actualizar(
          @PathVariable UUID id,
          @Valid @RequestBody ProductoRequest request) {

   return ResponseEntity.ok(
           mapper.toResponse(
                   obtenerProductosUseCase.obtenerPorId(
                           actualizarProductoUseCase
                                   .ejecutar(id, mapper.toDomain(request), mapper.toVariantesDomain(request.getVariants()))
                                           .getId())));
  }

  // DELETE /api/productos/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
    eliminarProductoUseCase.ejecutar(id);
    return ResponseEntity.noContent().build();
  }
}
