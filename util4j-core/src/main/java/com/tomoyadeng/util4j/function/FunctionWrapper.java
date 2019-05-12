package com.tomoyadeng.util4j.function;

import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.Function;

public class FunctionWrapper {
  private static final FunctionWrapper SHARED_WRAPPER = new FunctionWrapper(null);

  private final ExceptionHandler handler;

  public static FunctionWrapper sharedInstance() {
    return SHARED_WRAPPER;
  }

  public static FunctionWrapper instance(ExceptionHandler handler) {
    Objects.requireNonNull(handler);
    return new FunctionWrapper(handler);
  }

  private FunctionWrapper(ExceptionHandler handler) {
    this.handler = handler;
  }

  public <T, R> Function<T, R> unchecked(CheckedFunction<T, R> function) {
    if (this.handler == null) {
      return function.unchecked();
    } else {
      return function.unchecked(handler::onException);
    }
  }

  public <T, R> Function<T, R> orNull(CheckedFunction<T, R> function) {
    if (this.handler == null) {
      return function.orNull();
    } else {
      return function.orNull(handler::onException);
    }
  }

  @FunctionalInterface
  public interface ExceptionHandler {
    void onException(Exception e);
  }

  public static class PrintStreamExceptionHandler implements ExceptionHandler {
    private PrintStreamExceptionHandler() {}

    public static PrintStreamExceptionHandler getHandler() {
      return new PrintStreamExceptionHandler();
    }

    @Override
    public void onException(Exception e) {
      e.printStackTrace();
    }
  }

  public static class LoggerExceptionHandler implements ExceptionHandler {
    private static final String DEFAULT_TEMPLATE = "Exception occurred ";

    private final Logger logger;
    private final String template;

    private LoggerExceptionHandler(Logger logger, String template) {
      this.logger = logger;
      this.template = template;
    }

    public static LoggerExceptionHandler getHandler(Logger logger) {
      Objects.requireNonNull(logger);
      return new LoggerExceptionHandler(logger, DEFAULT_TEMPLATE);
    }

    public static LoggerExceptionHandler getHandler(Logger logger, String template) {
      Objects.requireNonNull(logger);
      return new LoggerExceptionHandler(logger, template);
    }

    @Override
    public void onException(Exception e) {
      logger.error(this.template, e);
    }
  }
}
