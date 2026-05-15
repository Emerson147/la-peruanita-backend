package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.domain.model.MovimientoInventario;
import com.emersondev.domain.repository.MovimientoRepository;
import com.emersondev.infrastructure.persistence.mapper.MovimientoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MovimientoRepositoryAdapter
        implements MovimientoRepository {

  private final MovimientoJpaRepository jpaRepository;
  private final MovimientoMapper mapper;

  @Override
  public MovimientoInventario save(MovimientoInventario m) {
    return mapper.toDomain(
            jpaRepository.save(mapper.toEntity(m)));
  }

  @Override
  public Optional<MovimientoInventario> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public List<MovimientoInventario> findAll() {
    return jpaRepository.findAll().stream()
            .map(mapper::toDomain).toList();
  }

  @Override
  public List<MovimientoInventario> findByProductId(UUID productId) {
    return jpaRepository.findByProductId(productId).stream()
            .map(mapper::toDomain).toList();
  }

  @Override
  public List<MovimientoInventario> findByType(String type) {
    return jpaRepository.findByType(type).stream()
            .map(mapper::toDomain).toList();
  }

  @Override
  public List<MovimientoInventario> findByFecha(
          LocalDateTime desde, LocalDateTime hasta) {
    return jpaRepository.findByFecha(desde, hasta).stream()
            .map(mapper::toDomain).toList();
  }

  @Override
  public long countByType(String type) {
    return jpaRepository.countByType(type);
  }

  @Override
  public Page<MovimientoInventario> findAllPaginado(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(mapper::toDomain);
  }

  @Override
  public Page<MovimientoInventario> findByTypePaginado(String type, Pageable pageable) {
    return jpaRepository.findByType(type, pageable).map(mapper::toDomain);
  }
}