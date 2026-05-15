package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.domain.model.Producto;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.infrastructure.persistence.entity.ProductoEntity;
import com.emersondev.infrastructure.persistence.mapper.ProductoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductoRepositoryAdapter implements ProductoRepository {

  private final ProductoJpaRepository jpaRepository;
  private final ProductoMapper mapper;

  @Override
  public Producto save(Producto producto) {
    ProductoEntity entity = mapper.toEntity(producto);
    ProductoEntity savedEntity = jpaRepository.save(entity);
    return mapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Producto> findById(UUID id) {
    return jpaRepository.findById(id)
            .map(mapper::toDomain);
  }

  @Override
  public List<Producto> findAll() {
    return jpaRepository.findAll()
            .stream()
            .map(mapper::toDomain)
            .toList();
  }


  @Override
  public List<Producto> findByCategory(String categoria) {
    return jpaRepository.findByCategoria(categoria)
            .stream()
            .map(mapper::toDomain)
            .toList();
  }

  @Override
  public List<Producto> findByStatus(String status) {
    return jpaRepository.findByStatus(status)
            .stream()
            .map(mapper::toDomain)
            .toList();
  }

  @Override
  public List<Producto> findByStockBajo() {
    return jpaRepository.findProductosConStockBajo()
            .stream()
            .map(mapper::toDomain)
            .toList();
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
  public Page<Producto> findAllPaginado(Pageable pageable) {
    return jpaRepository.findAll(pageable)
            .map(mapper::toDomain);
  }

  @Override
  public Page<Producto> findByCategoriaPaginado(
          String category, Pageable pageable) {
    return jpaRepository.findByCategoria(category, pageable)
            .map(mapper::toDomain);
  }
}