/*
 Copyright 2010-2013 University of Stavanger, Norway

 Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package no.uis.service.component.fsimport.util;

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
    if (!(obj instanceof PropertyInfo)) {
      return false;
    }
    PropertyInfo piOther = (PropertyInfo)obj;

    return Objects.equals(this.propName, piOther.propName) && Objects.equals(this.get, piOther.get) && Objects.equals(this.set, piOther.set)
      && Objects.equals(this.isSet, piOther.isSet);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}