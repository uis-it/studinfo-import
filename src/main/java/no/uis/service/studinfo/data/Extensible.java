package no.uis.service.studinfo.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

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
