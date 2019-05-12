package com.tomoyadeng.util4j.function;

import com.tomoyadeng.util4j.function.exception.FunctionRuntimeException;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@FunctionalInterface
public interface CheckedBiConsumer<T, U> {
  void accept(T t, U u) throws Exception;

  default BiConsumer<T, U> unchecked() {
    return (t, u) -> {
      try {
        accept(t, u);
      } catch (Exception e) {
        wrapAndThrow(e);
      }
    };
  }

  default BiConsumer<T, U> unchecked(Consumer<Exception> c) {
    return (t, u) -> {
      try {
        accept(t, u);
      } catch (Exception e) {
        c.accept(e);
        wrapAndThrow(e);
      }
    };
  }

  default BiConsumer<T, U> ignore() {
    return (t, u) -> {
      try {
        accept(t, u);
      } catch (Exception e) {
        // ignore
      }
    };
  }

  default BiConsumer<T, U> ignore(Consumer<Exception> c) {
    return (t, u) -> {
      try {
        accept(t, u);
      } catch (Exception e) {
        c.accept(e);
      }
    };
  }

  default void wrapAndThrow(Exception e) {
    throw new FunctionRuntimeException(e);
  }
}
