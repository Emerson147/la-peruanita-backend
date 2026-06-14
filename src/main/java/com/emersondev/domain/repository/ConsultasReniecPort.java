package com.emersondev.domain.repository;

import com.emersondev.domain.model.PersonaReniec;
import java.util.Optional;

public interface ConsultasReniecPort {
    Optional<PersonaReniec> consultarPorDni(String dni);
}
