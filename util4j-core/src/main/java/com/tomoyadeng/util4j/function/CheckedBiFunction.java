package com.tomoyadeng.util4j.function;

import com.tomoyadeng.util4j.function.exception.FunctionRuntimeException;

import java.util.function.BiFunction;
import java.util.function.Consumer;

@FunctionalInterface
public interface CheckedBiFunction<T, U, R> {
  R apply(T t, U u) throws Exception;

  default BiFunction<T, U, R> unchecked() {
    return (t, u) -> {
      try {
        return apply(t, u);
      } catch (Exception e) {
        return wrapAndThrow(e);
      }
    };
  }

  default BiFunction<T, U, R> unchecked(Consumer<Exception> c) {
    return (t, u) -> {
      try {
        return apply(t, u);
      } catch (Exception e) {
        c.accept(e);
        return wrapAndThrow(e);
      }
    };
  }

  default BiFunction<T, U, R> orNull() {
    return (t, u) -> {
      try {
        return apply(t, u);
      } catch (Exception e) {
        return null;
      }
    };
  }

  default BiFunction<T, U, R> orNull(Consumer<Exception> c) {
    return (t, u) -> {
      try {
        return apply(t, u);
      } catch (Exception e) {
        c.accept(e);
        return null;
      }
    };
  }

  default R wrapAndThrow(Exception e) {
    throw new FunctionRuntimeException(e);
  }
}
