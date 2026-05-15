package com.emersondev.application.usecase.cliente;

import com.emersondev.domain.model.Cliente;
import com.emersondev.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrearClienteUseCase {

  private final ClienteRepository clienteRepository;

  @Transactional
  public Cliente ejecutar(Cliente cliente) {
    log.info("Creando nuevo cliente: {}", cliente.getName());

    // Defaults
    if (cliente.getTier() == null) {
      cliente.setTier("nuevo");
    }
    if (cliente.getTotalPurchases() == null) {
      cliente.setTotalPurchases(BigDecimal.ZERO);
    }
    cliente.setCreatedAt(LocalDateTime.now());

    Cliente creado = clienteRepository.save(cliente);
    log.info("Cliente creado con id: {}", creado.getId());
    return creado;
  }
}