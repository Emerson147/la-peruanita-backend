package com.emersondev.application.usecase.configuracion;

import com.emersondev.domain.model.Configuracion;
import com.emersondev.domain.repository.ConfiguracionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActualizarConfiguracionUseCase {

    private final ConfiguracionRepository configuracionRepository;
    private final ObtenerConfiguracionUseCase obtenerConfiguracionUseCase;

    @Transactional
    public Configuracion ejecutar(Configuracion configuracionActualizada) {
        log.info("Actualizando configuracion global");
        
        Configuracion configuracionActual = obtenerConfiguracionUseCase.ejecutar();
        configuracionActual.actualizar(configuracionActualizada);
        
        return configuracionRepository.save(configuracionActual);
    }
}
