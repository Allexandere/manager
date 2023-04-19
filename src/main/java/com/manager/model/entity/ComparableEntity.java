package com.manager.model.entity;

import java.io.Serializable;
import java.util.UUID;

public abstract class ComparableEntity implements Serializable {

  public abstract UUID getId();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ComparableEntity that = (ComparableEntity) o;
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return getId().hashCode();
  }
}
