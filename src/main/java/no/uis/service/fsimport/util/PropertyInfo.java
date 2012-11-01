package no.uis.service.fsimport.util;

import java.lang.reflect.Method;
import java.util.Objects;

public class PropertyInfo {

  private final String propName;
  private final Method get;
  private final Method set;
  private final Method isSet;

  public PropertyInfo(String propName, Method set, Method get, Method isSet) {
    this.propName = propName;
    this.set = set;
    this.get = get;
    this.isSet = isSet;
  }

  public String getPropName() {
    return propName;
  }

  public Method getGet() {
    return get;
  }

  public Method getSet() {
    return set;
  }

  public Method getIsSet() {
    return isSet;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj instanceof PropertyInfo == false) {
      return false;
    }
    PropertyInfo piOther = (PropertyInfo)obj;

    return Objects.equals(propName, piOther.propName) && Objects.equals(get, piOther.get) && Objects.equals(set, piOther.set)
      && Objects.equals(isSet, piOther.isSet);
  }
}