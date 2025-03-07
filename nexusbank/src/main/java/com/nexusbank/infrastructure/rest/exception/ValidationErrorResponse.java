package com.nexusbank.infrastructure.rest.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Extensión de ErrorResponse para los casos de errores de validación,
 * que incluye un mapa de campos con errores.
 */
public class ValidationErrorResponse extends ErrorResponse {
  private Map<String, String> errors;

  public ValidationErrorResponse() {
    super();
  }

  public ValidationErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path,
      Map<String, String> errors) {
    super(timestamp, status, error, message, path);
    this.errors = errors;
  }

  public Map<String, String> getErrors() {
    return errors;
  }

  public void setErrors(Map<String, String> errors) {
    this.errors = errors;
  }
}