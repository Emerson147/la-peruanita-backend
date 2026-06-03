package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.domain.model.Variante;
import com.emersondev.domain.repository.VarianteRepository;
import com.emersondev.infrastructure.persistence.mapper.VarianteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VarianteRepositoryAdapter implements VarianteRepository {

  private final VarianteJpaRepository jpaRepository;
  private final VarianteMapper mapper;
  private final com.emersondev.domain.repository.InventarioRepository inventarioRepository;

  private Variante populateInventarios(Variante domain) {
    if (domain == null) return null;
    domain.setInventarios(inventarioRepository.findByVarianteId(domain.getId()));
    return domain;
  }

  @Override
  public Variante save(Variante variante) {
    Variante saved = mapper.toDomain(jpaRepository.save(mapper.toEntity(variante)));
    return populateInventarios(saved);
  }

  @Override
  public Optional<Variante> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain).map(this::populateInventarios);
  }

  @Override
  public List<Variante> findByProductId(UUID productId) {
    return jpaRepository.findByProductId(productId)
            .stream()
            .map(mapper::toDomain)
            .map(this::populateInventarios)
            .toList();
  }

  @Override
  @Transactional
  public void deleteByProductId(UUID productId) {
    jpaRepository.deleteByProductId(productId);
  }

  @Override
  public boolean existsById(UUID id) {
    return jpaRepository.existsById(id);
  }

  @Override
  @Transactional
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
