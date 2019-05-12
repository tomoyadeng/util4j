package com.tomoyadeng.util4j.function;

import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
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

  public <T, R> Function<T, R> unchecked(CheckedFunction<T, R> f) {
    return f.unchecked(exceptionConsumer());
  }

  public <T, R> Function<T, R> orNull(CheckedFunction<T, R> f) {
    return f.orNull(exceptionConsumer());
  }

  public <T, U, R> BiFunction<T, U, R> unchecked(CheckedBiFunction<T, U, R> f) {
    return f.unchecked(exceptionConsumer());
  }

  public <T, U, R> BiFunction<T, U, R> orNull(CheckedBiFunction<T, U, R> f) {
    return f.orNull(exceptionConsumer());
  }

  public <T> Consumer<T> unchecked(CheckedConsumer<T> f) {
    return f.unchecked(exceptionConsumer());
  }

  public <T> Consumer<T> ignore(CheckedConsumer<T> f) {
    return f.ignore(exceptionConsumer());
  }

  public <T, U> BiConsumer<T, U> unchecked(CheckedBiConsumer<T, U> f) {
    return f.unchecked(exceptionConsumer());
  }

  public <T, U> BiConsumer<T, U> ignore(CheckedBiConsumer<T, U> f) {
    return f.ignore(exceptionConsumer());
  }

  private Consumer<Exception> exceptionConsumer() {
    // if handler not configured, do nothing
    if (this.handler == null) {
      return t -> {};
    }

    return handler::onException;
  }

  @FunctionalInterface
  public interface ExceptionHandler {
    void onException(Exception e);
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
