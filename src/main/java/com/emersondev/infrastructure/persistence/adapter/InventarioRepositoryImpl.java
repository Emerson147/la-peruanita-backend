package com.emersondev.infrastructure.persistence.adapter;

import com.emersondev.domain.model.Inventario;
import com.emersondev.domain.repository.InventarioRepository;
import com.emersondev.infrastructure.persistence.entity.AlmacenEntity;
import com.emersondev.infrastructure.persistence.entity.InventarioEntity;
import com.emersondev.infrastructure.persistence.mapper.InventarioMapper;
import com.emersondev.infrastructure.persistence.repository.InventarioRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InventarioRepositoryImpl implements InventarioRepository {

  private final InventarioRepositoryJpa jpaRepository;
  private final InventarioMapper mapper;
  
  // EntityManager or AlmacenRepositoryJpa to get references if needed.
  // For save, we might need Almacen reference. Since we only save existing instances usually from usecase:
  
  @Override
  public Inventario save(Inventario inventario) {
    InventarioEntity entity = mapper.toEntity(inventario);
    
    // Set up Almacen Reference (to avoid loading full object if we only have ID)
    if (inventario.getAlmacenId() != null) {
      AlmacenEntity almacenRef = new AlmacenEntity();
      almacenRef.setId(inventario.getAlmacenId());
      entity.setAlmacen(almacenRef);
    }
    
    // Si la entidad ya existe, preservamos el version
    if (inventario.getId() != null) {
      jpaRepository.findById(inventario.getId()).ifPresent(existing -> {
        entity.setVersion(existing.getVersion());
      });
    }

    InventarioEntity saved = jpaRepository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public Optional<Inventario> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<Inventario> findByVarianteIdAndAlmacenId(UUID varianteId, UUID almacenId) {
    return jpaRepository.findByVarianteIdAndAlmacenId(varianteId, almacenId).map(mapper::toDomain);
  }

  @Override
  public List<Inventario> findByVarianteId(UUID varianteId) {
    return jpaRepository.findByVarianteId(varianteId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<Inventario> findByAlmacenId(UUID almacenId) {
    return jpaRepository.findByAlmacenId(almacenId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
  }
}
