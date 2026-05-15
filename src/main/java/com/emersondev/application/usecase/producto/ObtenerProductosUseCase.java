package com.emersondev.application.usecase.producto;

import com.emersondev.domain.exception.ProductoNotFoundException;
import com.emersondev.domain.model.Producto;
import com.emersondev.domain.model.Variante;
import com.emersondev.domain.repository.ProductoRepository;
import com.emersondev.domain.repository.VarianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObtenerProductosUseCase {

  private final ProductoRepository productoRepository;
  private final VarianteRepository varianteRepository;

  // Obtener todos
  public Page<Producto> obtenerTodos(String categoria, Pageable pageable) {

    Page<Producto> page = categoria != null
            ? productoRepository
            .findByCategoriaPaginado(categoria, pageable)
            : productoRepository.findAllPaginado(pageable);

    cargarVariantes(page.getContent());
    return page;
  }

  // Obtener por ID
  public Producto obtenerPorId(UUID id) {
    Producto producto = productoRepository.findById(id)
            .orElseThrow(() ->
                    new ProductoNotFoundException("Producto no encontrado con ID: " + id.toString()));

    producto.setVariantes(varianteRepository.findByProductId(id));
    return producto;
  }

  // Obtener por categoría
  public List<Producto> obtenerPorCategoria(String category) {
    return productoRepository.findByCategory(category);
  }

  // Obtener solo activos
  public List<Producto> obtenerActivos() {
    List<Producto> productos =
            productoRepository.findByStatus("active");
    cargarVariantes(productos);
    return productos;
  }

  // Obtener con stock bajo
  public List<Producto> obtenerConStockBajo() {
    List <Producto> productos =
            productoRepository.findByStockBajo();
    cargarVariantes(productos);
    return productos;
  }

  // Obtener todas la variantes
  public List<Variante> obtenerVariantes(UUID productoId) {
    return varianteRepository.findByProductId(productoId);
  }


  private void cargarVariantes(List<Producto> productos) {
    productos.forEach(p ->
            p.setVariantes(
                    varianteRepository.findByProductId(
                            p.getId())));
  }
}
