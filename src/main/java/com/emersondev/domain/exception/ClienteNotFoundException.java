package com.emersondev.domain.exception;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(String id) {
      super("Cliente no encontrado con el Id: " + id);
    }
}
