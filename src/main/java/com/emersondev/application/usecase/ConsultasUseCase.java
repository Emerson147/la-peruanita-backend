package com.emersondev.application.usecase;

import com.emersondev.domain.model.PersonaReniec;
import com.emersondev.domain.repository.ConsultasReniecPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultasUseCase {

    private final ConsultasReniecPort consultasReniecPort;

    public Optional<PersonaReniec> consultarDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener 8 dígitos numéricos.");
        }
        return consultasReniecPort.consultarPorDni(dni);
    }
}
