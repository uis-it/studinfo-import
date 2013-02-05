package no.uis.service.component.studinfopdf.convert;

import java.util.Collection;

public class CollectionConverter extends AbstractStringConverter<Collection<?>> {

  @Override
  protected String convert(Collection<?> value) {
    StringBuilder sb = new StringBuilder();
    for (Object object : value) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append(StringConverterUtil.convert(object));
    }
    return sb.toString();
  }

}
