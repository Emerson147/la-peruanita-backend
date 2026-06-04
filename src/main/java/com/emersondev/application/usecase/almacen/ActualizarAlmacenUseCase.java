package com.emersondev.application.usecase.almacen;

import com.emersondev.domain.model.Almacen;
import com.emersondev.domain.repository.AlmacenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActualizarAlmacenUseCase {

  private final AlmacenRepository almacenRepository;

  public Almacen ejecutar(UUID id, Almacen almacenActualizado) {
    Almacen almacenExistente = almacenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Almacen no encontrado"));
    if (almacenActualizado.getNombre() != null) {
      almacenExistente.setNombre(almacenActualizado.getNombre());
    }
    if (almacenActualizado.getDireccion() != null) {
      almacenExistente.setDireccion(almacenActualizado.getDireccion());
    }
    almacenExistente.setActivo(almacenActualizado.isActivo());
    return almacenRepository.save(almacenExistente);
  }
}
