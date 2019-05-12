package com.tomoyadeng.util4j.function;

import java.util.List;

public interface TestService {
  default List<Long> getIds() throws ServiceException {
    throw new ServiceException();
  }

  default Entity getById(long id) throws ServiceException {
    throw new ServiceException();
  }
}
