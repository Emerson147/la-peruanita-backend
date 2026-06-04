package com.emersondev.application.usecase.almacen;
import com.emersondev.domain.model.Almacen;
import com.emersondev.domain.repository.AlmacenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObtenerAlmacenesUseCase {

  private final AlmacenRepository almacenRepository;

  public List<Almacen> obtenerTodos() {
    return almacenRepository.findAll();
  }
  public Almacen obtenerPorId(UUID id) {
    return almacenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Almacen no encontrado"));
  }
}
