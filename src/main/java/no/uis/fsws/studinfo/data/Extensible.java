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

package no.uis.fsws.studinfo.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Java object that is base class to all serialized object.
 * The object can be extended by using {@link #addProperty(String, Object)}. 
 */
@XmlTransient
public class Extensible implements Serializable {

  private static final long serialVersionUID = 5106796394802552042L;
  
  @XmlTransient
  private Map<String, Object> properties;
  
  public void addProperty(String propName, Object value) {
    getOrCreateProperties().put(propName, value);
  }

  @SuppressWarnings("unchecked")
  public <T> T property(String propName) {
    if (isSetProperties()) {
      return (T)properties.get(propName);
    }
    return null;
  }
  
  public Map<String, Object> getProperties() {
    return properties;
  }

  public boolean isSetProperties() {
    return properties != null;
  }
  
  private Map<String, Object> getOrCreateProperties() {
    if (properties == null) {
      synchronized(this) {
        if  (properties == null) {
          properties = new HashMap<String, Object>();
        }
      }
    }
    return properties;
  }
}
