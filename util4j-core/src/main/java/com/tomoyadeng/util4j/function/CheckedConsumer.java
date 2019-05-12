package com.tomoyadeng.util4j.function;

import com.tomoyadeng.util4j.function.exception.FunctionRuntimeException;

import java.util.function.Consumer;

@FunctionalInterface
public interface CheckedConsumer<T> {
  void accept(T t) throws Exception;

  default Consumer<T> unchecked() {
    return t -> {
      try {
        accept(t);
      } catch (Exception e) {
        wrapAndThrow(e);
      }
    };
  }

  default Consumer<T> unchecked(Consumer<Exception> c) {
    return t -> {
      try {
        accept(t);
      } catch (Exception e) {
        c.accept(e);
        wrapAndThrow(e);
      }
    };
  }

  default Consumer<T> ignore() {
    return t -> {
      try {
        accept(t);
      } catch (Exception e) {
        // ignore
      }
    };
  }

  default Consumer<T> ignore(Consumer<Exception> c) {
    return t -> {
      try {
        accept(t);
      } catch (Exception e) {
        c.accept(e);
      }
    };
  }

  default void wrapAndThrow(Exception e) {
    throw new FunctionRuntimeException(e);
  }
}
