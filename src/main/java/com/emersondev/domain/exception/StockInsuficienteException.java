package com.emersondev.domain.exception;

public class StockInsuficienteException extends RuntimeException {

  public StockInsuficienteException(String productoNombre) {

    super("Stock insuficiente para el producto: " + productoNombre);
  }
}
