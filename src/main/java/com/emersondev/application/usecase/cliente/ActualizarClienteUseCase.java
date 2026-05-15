package com.emersondev.application.usecase.cliente;

import com.emersondev.domain.exception.ClienteNotFoundException;
import com.emersondev.domain.model.Cliente;
import com.emersondev.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActualizarClienteUseCase {

  private final ClienteRepository clienteRepository;

  @Transactional
  public Cliente ejecutar(UUID id, Cliente clienteActualizado) {
    log.info("Actualizando cliente: {}", id);

    Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new ClienteNotFoundException(
                    id.toString()));

      // Solo actualizamos campos que no sean nulos en clienteActualizado
    cliente.actualizarPerfil(clienteActualizado);

    return clienteRepository.save(cliente);
  }


}