package com.emersondev.infrastructure.web.mapper;

import com.emersondev.domain.model.LiquidacionProducto;
import com.emersondev.domain.service.LiquidacionService;
import com.emersondev.infrastructure.web.dto.response.LiquidacionItemResponse;
import com.emersondev.infrastructure.web.dto.response.LiquidacionResumenResponse;
import org.springframework.stereotype.Component;

@Component
public class LiquidacionDtoMapper {

  public LiquidacionItemResponse toItemResponse(LiquidacionProducto liq) {
    LiquidacionItemResponse response = new LiquidacionItemResponse();

    response.setProductoId(liq.getProducto().getId());
    response.setNombre(liq.getProducto().getName());
    response.setCategoria(liq.getProducto().getCategoria());
    response.setStock(liq.getProducto().getStockTotal());
    response.setCosto(liq.getProducto().getCost());
    response.setPrecioActual(liq.getProducto().getPrice());
    response.setDiasSinVender(liq.getDiasSinVender());
    response.setFeriasSinVender(liq.getFeriasSinVender());
    response.setRotacionPorFeria(liq.getRotacionPorFeria());
    response.setEstado(liq.getEstado());
    response.setPrecioConDescuento20(liq.getPrecioConDescuento20());
    response.setPrecioConDescuento30(liq.getPrecioConDescuento30());
    response.setPrecioConDescuento40(liq.getPrecioConDescuento40());
    response.setPrecioCongelado(liq.getPrecioCongelado());
    response.setCapitalCongelado(liq.getCapitalCongelado());
    response.setPotencialRecuperacion(liq.getPotencialRecuperacion());

    return response;
  }

  public LiquidacionResumenResponse toResumenResponse(
          LiquidacionService.ResumenLiquidacion resumen) {

    LiquidacionResumenResponse response = new LiquidacionResumenResponse();
    response.setTotalCapital(resumen.totalCapital());
    response.setCapitalActivo(resumen.capitalActivo());
    response.setCapitalLento(resumen.capitalLento());
    response.setCapitalCongelado(resumen.capitalCongelado());
    response.setMetaLiberacion(resumen.metaLiberacion());
    response.setRatioLiquidez(resumen.ratioLiquidez());
    response.setProductosCongelados(resumen.productosCongelados());
    response.setProductosLentos(resumen.productosLentos());
    response.setProductosActivos(resumen.productosActivos());

    return response;
  }
}
