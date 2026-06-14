package com.emersondev.infrastructure.web.controller;

import com.emersondev.application.usecase.ConsultasUseCase;
import com.emersondev.domain.model.PersonaReniec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/consultas")
@RequiredArgsConstructor
public class ConsultasController {

    private final ConsultasUseCase consultasUseCase;

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PersonaReniec> consultarDni(@PathVariable String dni) {
        return consultasUseCase.consultarDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
