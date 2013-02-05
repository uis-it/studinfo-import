package no.uis.service.component.studinfopdf.convert;

import no.uis.service.studinfo.data.Hjelpemiddel;

public class HjelpemiddelConverter extends AbstractStringConverter<Hjelpemiddel> {

  @Override
  protected String convert(Hjelpemiddel value) {
    StringBuilder sb = new StringBuilder();
    
    sb.append(StringConverterUtil.convert(value.getHjelpemiddelnavn()));
    if (value.isSetHjelpemiddelmerknad() && !value.getHjelpemiddelmerknad().isEmpty()) {
      sb.append(" ("); //$NON-NLS-1$
      sb.append(StringConverterUtil.convert(value.getHjelpemiddelmerknad()));
      sb.append(')');
    }
    return sb.toString();
  }
}
