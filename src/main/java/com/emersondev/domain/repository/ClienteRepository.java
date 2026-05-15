package com.emersondev.domain.repository;

import com.emersondev.domain.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository {

  Cliente save(Cliente cliente);
  Optional<Cliente> findById(UUID id);
  Optional<Cliente> findByPhone(String phone);
  List<Cliente> findAll();
  List<Cliente> findByTier(String tier);
  void deleteById(UUID id);
  boolean existsById(UUID id);

  Page<Cliente> findAllPaginado(Pageable pageable);
  Page<Cliente> findByTierPaginado(String tier, Pageable pageable);
}
