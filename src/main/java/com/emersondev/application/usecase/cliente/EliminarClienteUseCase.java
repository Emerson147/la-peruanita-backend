package com.emersondev.application.usecase.cliente;

import com.emersondev.domain.repository.ClienteRepository;
import com.emersondev.domain.exception.ClienteNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EliminarClienteUseCase {

    private final ClienteRepository clienteRepository;

    @Transactional
    public void ejecutar(UUID id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNotFoundException(id.toString());
        }
        clienteRepository.deleteById(id);
    }
}
