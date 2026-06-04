package com.emersondev.application.usecase.almacen;
import com.emersondev.domain.model.Almacen;
import com.emersondev.domain.repository.AlmacenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class EliminarAlmacenUseCase {
  private final AlmacenRepository almacenRepository;
  public void ejecutar(UUID id) {
    Almacen almacen = almacenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Almacen no encontrado"));
    almacen.setActivo(false);
    almacenRepository.save(almacen);
  }
}
