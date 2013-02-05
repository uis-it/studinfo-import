package no.uis.service.component.studinfopdf.convert;

import java.util.Map;

public class MapConverter extends AbstractStringConverter<Map<?, ?>> {

  @Override
  protected String convert(Map<?, ?> value) {
    return StringConverterUtil.convert(value.entrySet());
  }
}
