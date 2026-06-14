package com.emersondev.infrastructure.persistence.mapper;

import com.emersondev.domain.model.Cliente;
import com.emersondev.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

  public ClienteEntity toEntity(Cliente cliente) {
    if (cliente == null) return null;

    ClienteEntity entity = new ClienteEntity();
    entity.setId(cliente.getId());
    entity.setName(cliente.getName());
    entity.setDocumentNumber(cliente.getDocumentNumber());
    entity.setPhone(cliente.getPhone());
    entity.setEmail(cliente.getEmail());
    entity.setAddress(cliente.getAddress());
    entity.setTotalPurchases(cliente.getTotalPurchases());
    entity.setLastPurchaseDate(cliente.getLastPurchaseDate());
    entity.setTier(cliente.getTier() != null
            ? cliente.getTier() : "nuevo");
    entity.setPreferences(cliente.getPreferences());
    entity.setCreatedAt(cliente.getCreatedAt());

    return entity;
  }

  public Cliente toDomain(ClienteEntity entity) {
    if (entity == null) return null;

    Cliente cliente = new Cliente();
    cliente.setId(entity.getId());
    cliente.setName(entity.getName());
    cliente.setDocumentNumber(entity.getDocumentNumber());
    cliente.setPhone(entity.getPhone());
    cliente.setEmail(entity.getEmail());
    cliente.setAddress(entity.getAddress());
    cliente.setTotalPurchases(entity.getTotalPurchases());
    cliente.setLastPurchaseDate(entity.getLastPurchaseDate());
    cliente.setTier(entity.getTier());
    cliente.setPreferences(entity.getPreferences());
    cliente.setCreatedAt(entity.getCreatedAt());

    return cliente;
  }
}