package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.domain.model.Usuario;
import com.emersondev.infrastructure.persistence.mapper.UsuarioMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter {

  private final UsuarioJpaRepository jpaRepository;
  private final UsuarioMapper mapper;

  public UsuarioRepositoryAdapter(
      UsuarioJpaRepository jpaRepository,
      UsuarioMapper mapper) {
    this.jpaRepository = jpaRepository;
    this.mapper = mapper;
  }

  public Usuario save(Usuario usuario) {
    return mapper.toDomain(
        jpaRepository.save(mapper.toEntity(usuario)));
  }

  public Optional<Usuario> findByEmail(String email) {
    return jpaRepository.findByEmail(email)
        .map(mapper::toDomain);
  }

  public boolean existsByEmail(String email) {
    return jpaRepository.existsByEmail(email);
  }

  public List<Usuario> findAll() {
    return jpaRepository.findAll().stream()
        .map(mapper::toDomain).toList();
  }

  public Optional<Usuario> findById(java.util.UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }
}

