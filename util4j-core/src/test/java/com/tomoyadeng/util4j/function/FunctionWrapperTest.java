package com.tomoyadeng.util4j.function;

import com.tomoyadeng.util4j.function.exception.FunctionRuntimeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FunctionWrapperTest {
  private FunctionWrapper wrapper;
  private TestService service;

  @Before
  public void setup() {
    this.wrapper = FunctionWrapper.sharedInstance();
    this.service = new TestService() {};
  }

  @Test(expected = FunctionRuntimeException.class)
  public void testFunctionUnchecked() {
    List<Long> ids = List.of(1L, 2L, 3L);
    List<Entity> entities =
        ids.stream().map(wrapper.unchecked(service::getById)).collect(Collectors.toList());
  }

  @Test
  public void testFunctionOrNull() {
    List<Long> ids = List.of(1L, 2L, 3L);
    List<Entity> entities =
        ids.stream()
            .map(wrapper.orNull(service::getById))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    Assert.assertEquals(0, entities.size());
  }
}
