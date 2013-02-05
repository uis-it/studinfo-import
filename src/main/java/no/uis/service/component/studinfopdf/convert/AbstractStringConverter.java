package no.uis.service.component.studinfopdf.convert;

import org.apache.log4j.Logger;

public abstract class AbstractStringConverter<T> implements StringConverter {

  private static final Logger log = Logger.getLogger(AbstractStringConverter.class);
  
  @SuppressWarnings("unchecked")
  @Override
  public String convertToString(Object value) {
    if (value == null) {
      return null;
    }
    try {
      return convert((T)value);
    } catch(Exception ex) {
      log.warn(value, ex);
    }
    return null;
  }
  
  protected abstract String convert(T value);
}
