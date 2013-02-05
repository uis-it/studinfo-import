package no.uis.service.component.studinfopdf.convert;

import java.util.regex.Pattern;

public class StringStringConverter extends AbstractStringConverter<String> {

  private Pattern pattern = Pattern.compile("\\s+"); //$NON-NLS-1$
  @Override
  protected String convert(String value) {
    return pattern.matcher(value).replaceAll(" "); //$NON-NLS-1$ 
  }
}
