package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.domain.model.Cliente;
import com.emersondev.domain.repository.ClienteRepository;
import com.emersondev.infrastructure.persistence.mapper.ClienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClienteRepositoryAdapter implements ClienteRepository {

  private final ClienteJpaRepository jpaRepository;
  private final ClienteMapper mapper;

  @Override
  public Cliente save(Cliente cliente) {
    return mapper.toDomain(
            jpaRepository.save(mapper.toEntity(cliente)));
  }

  @Override
  public Optional<Cliente> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<Cliente> findByPhone(String phone) {
    return jpaRepository.findByPhone(phone).map(mapper::toDomain);
  }

  @Override
  public List<Cliente> findAll() {
    return jpaRepository.findAll().stream()
            .map(mapper::toDomain).toList();
  }

  @Override
  public List<Cliente> findByTier(String tier) {
    return jpaRepository.findByTier(tier).stream()
            .map(mapper::toDomain).toList();
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }

  @Override
  public boolean existsById(UUID id) {
    return jpaRepository.existsById(id);
  }

  @Override
  public Page<Cliente> findAllPaginado(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(mapper::toDomain);
  }

  @Override
  public Page<Cliente> findByTierPaginado(String tier, Pageable pageable) {
    return jpaRepository.findByTier(tier, pageable).map(mapper::toDomain);
  }
}