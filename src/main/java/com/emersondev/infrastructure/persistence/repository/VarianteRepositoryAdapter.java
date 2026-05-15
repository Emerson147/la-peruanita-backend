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

  @Override
  public Variante save(Variante variante) {
    return mapper.toDomain(jpaRepository.save(mapper.toEntity(variante)));
  }

  @Override
  public Optional<Variante> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public List<Variante> findByProductId(UUID productId) {
    return jpaRepository.findByProductId(productId)
            .stream()
            .map(mapper::toDomain)
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
