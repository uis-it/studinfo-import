package no.uis.service.component.studinfopdf.convert;

import java.util.HashMap;
import java.util.Map;

public class StringConverterUtil {

  private static StringConverterUtil singleton;
  
  private Map<Class<?>, StringConverter> converters = new HashMap<Class<?>, StringConverter>();
  private StringConverter defaultConverter = new AbstractStringConverter<Object>() {
    @Override
    protected String convert(Object value) {
      return value.toString();
    }
  };
  
  private StringConverterUtil() {
  }
  
  private void register(StringConverter converter, Class<?> clazz) {
    converters.put(clazz, converter == null ? defaultConverter : converter);
  }

  protected String convertToString(Object value) {
    if (value != null) {
      return convertToString(value, value.getClass());
    }
    return convertToString(value, Object.class);
  }
  
  protected String convertToString(Object value, Class<?> valueType) {

    return getConverter(valueType).convertToString(value);
  }
  
  private StringConverter getConverter(Class<?> valueType) {
    if (valueType != null) {
      StringConverter converter = converters.get(valueType);
      if (converter != null) {
        return converter;
      }
      for (Class<?> iface : valueType.getInterfaces()) {
        converter = converters.get(iface);
        if (converter != null) {
          return converter;
        }
      }
      return getConverter(valueType.getSuperclass());
    }
    return defaultConverter;
  }
  
  public static String convert(Object value) {
    return getInstance().convertToString(value);
  }
  
  public static String convert(Object value, Class<?> valueType) {
    return getInstance().convertToString(value, valueType);
  }

  private static StringConverterUtil getInstance() {
    if (singleton == null) {
      synchronized(StringConverterUtil.class) {
        if (singleton == null) {
          singleton = new StringConverterUtil();
        }
      }
    }
    return singleton;
  }

  /**
   * Register a converter for a given class. 
   * @param converter if {@code null}, the default converter is used for the given class.
   * @param valueType the type for which the converter is registered
   */
  public static void registerConverter(StringConverter converter, Class<?> valueType) {
    getInstance().register(converter, valueType);
  }
}
