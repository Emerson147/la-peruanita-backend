package com.emersondev.domain.exception;

public class GastoNotFoundException extends RuntimeException {
    public GastoNotFoundException(String id) {
      super("Gasto no encontrado con el Id: " + id);
    }
}
