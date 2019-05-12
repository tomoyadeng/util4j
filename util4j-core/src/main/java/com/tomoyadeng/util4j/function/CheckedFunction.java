package com.tomoyadeng.util4j.function;

import com.tomoyadeng.util4j.function.exception.FunctionRuntimeException;

import java.util.function.Consumer;
import java.util.function.Function;

public interface CheckedFunction<T, R> {
  R apply(T t) throws Exception;

  default Function<T, R> unchecked() {
    return t -> {
      try {
        return apply(t);
      } catch (Exception e) {
        return wrapAndThrow(e);
      }
    };
  }

  default Function<T, R> unchecked(Consumer<Exception> c) {
    return t -> {
      try {
        return apply(t);
      } catch (Exception e) {
        c.accept(e);
        return wrapAndThrow(e);
      }
    };
  }

  default Function<T, R> orNull() {
    return t -> {
      try {
        return apply(t);
      } catch (Exception e) {
        return null;
      }
    };
  }

  default Function<T, R> orNull(Consumer<Exception> c) {
    return t -> {
      try {
        return apply(t);
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
