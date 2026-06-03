package com.emersondev.application.usecase.cliente;

import com.emersondev.domain.model.Cliente;
import com.emersondev.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObtenerClientesUseCase {

  private final ClienteRepository clienteRepository;

  public Page<Cliente> obtenerTodos(String tier, Pageable pageable) {
      return tier != null
            ? clienteRepository.findByTierPaginado(
              tier, pageable)
            : clienteRepository.findAllPaginado(pageable);
  }

  public Optional<Cliente> obtenerPorId(UUID id) {
    return clienteRepository.findById(id);
  }

  public List<Cliente> obtenerPorTier(String tier) {
    return clienteRepository.findByTier(tier);
  }

  public Optional<Cliente> obtenerPorTelefono(String phone) {
    return clienteRepository.findByPhone(phone);
  }
}