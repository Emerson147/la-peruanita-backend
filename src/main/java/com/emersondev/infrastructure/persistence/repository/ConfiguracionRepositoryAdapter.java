package com.emersondev.infrastructure.persistence.repository;

import com.emersondev.domain.model.Configuracion;
import com.emersondev.domain.repository.ConfiguracionRepository;
import com.emersondev.infrastructure.persistence.entity.ConfiguracionEntity;
import com.emersondev.infrastructure.persistence.mapper.ConfiguracionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConfiguracionRepositoryAdapter implements ConfiguracionRepository {

    private final ConfiguracionJpaRepository jpaRepository;
    private final ConfiguracionMapper mapper;

    @Override
    public Optional<Configuracion> obtenerConfiguracionGlobal() {
        return jpaRepository.findAll().stream()
                .findFirst()
                .map(mapper::toDomain);
    }

    @Override
    public Configuracion save(Configuracion configuracion) {
        ConfiguracionEntity entity = mapper.toEntity(configuracion);
        ConfiguracionEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
