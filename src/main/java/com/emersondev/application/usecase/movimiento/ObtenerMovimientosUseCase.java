package com.emersondev.application.usecase.movimiento;

import com.emersondev.domain.model.MovimientoInventario;
import com.emersondev.domain.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObtenerMovimientosUseCase {

  private final MovimientoRepository movimientoRepository;

  public List<MovimientoInventario> obtenerTodos() {
    return movimientoRepository.findAll();
  }

  public Page<MovimientoInventario> obtenerTodosPaginado(Pageable pageable) {
    return movimientoRepository.findAllPaginado(pageable);
  }

  public List<MovimientoInventario> obtenerPorProducto(
          UUID productId) {
    return movimientoRepository.findByProductId(productId);
  }

  public List<MovimientoInventario> obtenerPorTipo(String type) {
    return movimientoRepository.findByType(type);
  }

  public Page<MovimientoInventario> obtenerPorTipoPaginado(String type, Pageable pageable) {
    return movimientoRepository.findByTypePaginado(type, pageable);
  }

  public List<MovimientoInventario> obtenerPorFecha(
          LocalDateTime desde, LocalDateTime hasta) {
    return movimientoRepository.findByFecha(desde, hasta);
  }
}