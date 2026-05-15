package com.emersondev.domain.exception;

public class EmailYaRegistradoException extends RuntimeException {

  public EmailYaRegistradoException(String email) {
    super("Email ya esta registrado: " + email);
  }
}
