package com.emersondev.domain.repository;

import com.emersondev.domain.model.Configuracion;
import java.util.Optional;

public interface ConfiguracionRepository {
    Optional<Configuracion> obtenerConfiguracionGlobal();
    Configuracion save(Configuracion configuracion);
}
