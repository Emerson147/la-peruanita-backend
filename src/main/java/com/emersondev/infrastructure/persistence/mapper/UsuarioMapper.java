package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.Usuario;
import com.emersondev.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

  public UsuarioEntity toEntity(Usuario usuario) {
    if (usuario == null) return null;

    UsuarioEntity entity = new UsuarioEntity();
    entity.setId(usuario.getId());
    entity.setNombre(usuario.getNombre());
    entity.setEmail(usuario.getEmail());
    entity.setPassword(usuario.getPassword());
    entity.setRol(usuario.getRol());
    entity.setActivo(usuario.isActivo());
    entity.setCreatedAt(usuario.getCreatedAt());

    return entity;
  }

  public Usuario toDomain(UsuarioEntity entity) {
    if (entity == null) return null;

    Usuario usuario = new Usuario();
    usuario.setId(entity.getId());
    usuario.setNombre(entity.getNombre());
    usuario.setEmail(entity.getEmail());
    usuario.setPassword(entity.getPassword());
    usuario.setRol(entity.getRol());
    usuario.setActivo(entity.isActivo());
    usuario.setCreatedAt(entity.getCreatedAt());

    return usuario;
  }
}
