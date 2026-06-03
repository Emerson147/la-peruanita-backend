package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.domain.model.Venta;
import com.emersondev.domain.repository.VentaRepository;
import com.emersondev.infrastructure.persistence.mapper.VentaMapper;
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
public class VentaRepositoryAdapter implements VentaRepository {

  private final VentaJpaRepository jpaRepository;
  private final VentaMapper mapper;

  @Override
  public Venta save(Venta venta) {
    return mapper.toDomain(jpaRepository.save(mapper.toEntity(venta)));
  }

  @Override
  public Optional<Venta> findById(UUID id) {
    return jpaRepository.findById(id)
            .map(mapper::toDomain);
  }

  @Override
  public Optional<Venta> findBySaleNumber(String saleNumber) {
    return jpaRepository.findBySaleNumber(saleNumber).map(mapper::toDomain);
  }

  @Override
  public List<Venta> findAll() {
    return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
  }

  @Override
  public List<Venta> findByUserId(UUID userId) {
    return jpaRepository.findByUserId(userId).stream().map(mapper::toDomain).toList();
  }

  @Override
  public List<Venta> findByStatus(String status) {
    return jpaRepository.findByStatus(status).stream().map(mapper::toDomain).toList();
  }

  @Override
  public List<Venta> findByFecha(LocalDateTime desde, LocalDateTime hasta) {
    return jpaRepository.findByFecha(desde, hasta).stream().map(mapper::toDomain).toList();
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
  public Long countAll() {
    return jpaRepository.count();
  }

  @Override
  public Page<Venta> findAllPaginado(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(mapper::toDomain);
  }

  @Override
  public Page<Venta> findByStatusPaginado(String status, Pageable pageable) {
    return jpaRepository.findByStatus(status, pageable).map(mapper::toDomain);
  }
}
