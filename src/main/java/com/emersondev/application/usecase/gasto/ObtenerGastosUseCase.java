package com.emersondev.application.usecase.gasto;

import com.emersondev.domain.exception.GastoNotFoundException;
import com.emersondev.domain.model.Gasto;
import com.emersondev.domain.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObtenerGastosUseCase {

  private final GastoRepository gastoRepository;

  public List<Gasto> obtenerTodos() {
    return gastoRepository.findAll();
  }

  public Gasto obtenerPorId(UUID id) {
    return gastoRepository.findById(id)
            .orElseThrow(() ->
                    new GastoNotFoundException(id.toString()));
  }

  public List<Gasto> obtenerPorCategoria(String category) {
    return gastoRepository.findByCategory(category);
  }

  public List<Gasto> obtenerPorFecha(
          LocalDateTime desde, LocalDateTime hasta) {
    return gastoRepository.findByFecha(desde, hasta);
  }
}