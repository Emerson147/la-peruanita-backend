package com.emersondev.infrastructure.web.mapper;

import com.emersondev.domain.model.Cliente;
import com.emersondev.infrastructure.web.dto.request.ClienteRequest;
import com.emersondev.infrastructure.web.dto.response.ClienteResponse;
import org.springframework.stereotype.Component;

@Component
public class ClienteDtoMapper {

  public Cliente toDomain(ClienteRequest request) {
    if (request == null) return null;

    Cliente cliente = new Cliente();
    cliente.setName(request.getName());
    cliente.setPhone(request.getPhone());
    cliente.setEmail(request.getEmail());
    cliente.setAddress(request.getAddress());
    cliente.setPreferences(request.getPreferences());

    return cliente;
  }

  public ClienteResponse toResponse(Cliente cliente) {
    if (cliente == null) return null;

    ClienteResponse response = new ClienteResponse();
    response.setId(cliente.getId());
    response.setName(cliente.getName());
    response.setPhone(cliente.getPhone());
    response.setEmail(cliente.getEmail());
    response.setAddress(cliente.getAddress());
    response.setTotalPurchases(cliente.getTotalPurchases());
    response.setLastPurchaseDate(cliente.getLastPurchaseDate());
    response.setTier(cliente.getTier());
    response.setPreferences(cliente.getPreferences());
    response.setCreatedAt(cliente.getCreatedAt());

    return response;
  }
}