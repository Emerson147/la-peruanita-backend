package com.emersondev.infrastructure.persistence.adapter;

import com.emersondev.domain.model.Almacen;
import com.emersondev.domain.repository.AlmacenRepository;
import com.emersondev.infrastructure.persistence.entity.AlmacenEntity;
import com.emersondev.infrastructure.persistence.mapper.AlmacenMapper;
import com.emersondev.infrastructure.persistence.repository.AlmacenRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AlmacenRepositoryAdapter implements AlmacenRepository {

  private final AlmacenRepositoryJpa jpaRepository;
  private final AlmacenMapper mapper;

  @Override
  public Almacen save(Almacen almacen) {
    AlmacenEntity entity = mapper.toEntity(almacen);
    AlmacenEntity saved = jpaRepository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public Optional<Almacen> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<Almacen> findByNombre(String nombre) {
    return jpaRepository.findByNombre(nombre).map(mapper::toDomain);
  }

  @Override
  public List<Almacen> findAll() {
    return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
  }
}
