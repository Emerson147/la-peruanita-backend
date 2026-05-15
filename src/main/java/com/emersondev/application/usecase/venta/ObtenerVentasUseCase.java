package com.emersondev.application.usecase.venta;

import com.emersondev.domain.exception.VentaNotFoundException;
import com.emersondev.domain.model.Venta;
import com.emersondev.domain.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObtenerVentasUseCase {

  private final VentaRepository ventaRepository;

  public Page<Venta> obtenerTodos(
          String status,
          LocalDateTime desde,
          LocalDateTime hasta,
          Pageable pageable) {

    //Con fechas
    if (desde != null && hasta != null) {
      return new PageImpl<>(
              ventaRepository.findByFecha(desde, hasta),
              pageable,
              ventaRepository.findByFecha(desde, hasta).size());
    }

    // Con status
    if (status != null) {
      return ventaRepository
              .findByStatusPaginado(status,  pageable);
    }

    // Sin filtros
    return ventaRepository.findAllPaginado(pageable);

  }

  public Venta obtenerPorId(UUID id) {
    log.info("Obteniendo venta con id: {}", id);
    return ventaRepository.findById(id)
            .orElseThrow(() -> new VentaNotFoundException(id.toString()));
  }

  public Venta obtenerPorNumero(String saleNumber) {
    log.info("Obteniendo venta con número: {}", saleNumber);
    return ventaRepository.findBySaleNumber(saleNumber)
            .orElseThrow(() -> new VentaNotFoundException(saleNumber));
  }

  public List<Venta> obtenerPorFecha(LocalDateTime desde, LocalDateTime hasta) {
    log.info("Obteniendo ventas entre {} y {}", desde, hasta);
    return ventaRepository.findByFecha(desde, hasta);
  }

  public List<Venta> obtenerPorStatus(String status) {
    return ventaRepository.findByStatus(status);
  }
}
