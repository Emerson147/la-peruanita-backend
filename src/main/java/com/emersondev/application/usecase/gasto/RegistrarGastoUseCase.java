package com.emersondev.application.usecase.gasto;

import com.emersondev.domain.model.Gasto;
import com.emersondev.domain.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrarGastoUseCase {

  private final GastoRepository gastoRepository;

  @Transactional
  public Gasto ejecutar(Gasto gasto) {
    log.info("Registrando gasto: {} - S/ {}",
            gasto.getDescription(), gasto.getAmount());

    if (gasto.getCategory() == null) {
      gasto.setCategory("other");
    }

    gasto.setCreatedAt(LocalDateTime.now());

    Gasto registrado = gastoRepository.save(gasto);
    log.info("Gasto registrado con id: {}", registrado.getId());
    return registrado;
  }
}