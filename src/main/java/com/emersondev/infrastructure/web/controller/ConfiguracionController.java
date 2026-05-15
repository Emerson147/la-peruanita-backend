package com.emersondev.infrastructure.web.controller;

import com.emersondev.application.usecase.configuracion.ActualizarConfiguracionUseCase;
import com.emersondev.application.usecase.configuracion.ObtenerConfiguracionUseCase;
import com.emersondev.domain.model.Configuracion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class ConfiguracionController {

    private final ObtenerConfiguracionUseCase obtenerConfiguracionUseCase;
    private final ActualizarConfiguracionUseCase actualizarConfiguracionUseCase;

    @GetMapping
    public ResponseEntity<Configuracion> obtenerConfiguracion() {
        return ResponseEntity.ok(obtenerConfiguracionUseCase.ejecutar());
    }

    @PutMapping
    public ResponseEntity<Configuracion> actualizarConfiguracion(@RequestBody Configuracion configuracion) {
        return ResponseEntity.ok(actualizarConfiguracionUseCase.ejecutar(configuracion));
    }
}
