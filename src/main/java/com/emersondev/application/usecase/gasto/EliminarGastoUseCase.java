package com.emersondev.application.usecase.gasto;

import com.emersondev.domain.exception.GastoNotFoundException;
import com.emersondev.domain.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EliminarGastoUseCase {

  private final GastoRepository gastoRepository;

  @Transactional
  public void ejecutar(UUID id) {
    if (!gastoRepository.existsById(id)) {
      throw new GastoNotFoundException(id.toString());
    }
    gastoRepository.deleteById(id);
    log.info("Gasto eliminado: {}", id);
  }
}