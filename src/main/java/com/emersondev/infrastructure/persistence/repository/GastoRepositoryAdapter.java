package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.domain.model.Gasto;
import com.emersondev.domain.repository.GastoRepository;
import com.emersondev.infrastructure.persistence.mapper.GastoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GastoRepositoryAdapter implements GastoRepository {

  private final GastoJpaRepository jpaRepository;
  private final GastoMapper mapper;

  @Override
  public Gasto save(Gasto gasto) {
    return mapper.toDomain(
            jpaRepository.save(mapper.toEntity(gasto)));
  }

  @Override
  public Optional<Gasto> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public List<Gasto> findAll() {
    return jpaRepository.findAll().stream()
            .map(mapper::toDomain).toList();
  }

  @Override
  public List<Gasto> findByCategory(String category) {
    return jpaRepository.findByCategory(category).stream()
            .map(mapper::toDomain).toList();
  }

  @Override
  public List<Gasto> findByFecha(
          LocalDateTime desde, LocalDateTime hasta) {
    return jpaRepository.findByFecha(desde, hasta).stream()
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
}