package com.tomoyadeng.util4j.function.exception;

/**
 * FunctionRuntimeException
 */
public class FunctionRuntimeException extends RuntimeException {
  public FunctionRuntimeException() {}

  public FunctionRuntimeException(String message) {
    super(message);
  }

  public FunctionRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public FunctionRuntimeException(Throwable cause) {
    super(cause);
  }

  public FunctionRuntimeException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
