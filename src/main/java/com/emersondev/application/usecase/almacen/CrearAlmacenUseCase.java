package com.emersondev.application.usecase.almacen;

import com.emersondev.domain.model.Almacen;
import com.emersondev.domain.repository.AlmacenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrearAlmacenUseCase {

  private final AlmacenRepository almacenRepository;

  public Almacen ejecutar(Almacen almacen) {
    if (almacen.getNombre() == null || almacen.getNombre().isEmpty()) {
      throw new IllegalArgumentException("El nombre del almacen es obligatorio");
    }
    almacen.setActivo(true);
    if (almacen.getDireccion() == null) {
      almacen.setDireccion("Direccion por defecto");
    }
    return almacenRepository.save(almacen);
  }
}
