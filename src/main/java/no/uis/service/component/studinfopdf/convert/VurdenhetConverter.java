package no.uis.service.component.studinfopdf.convert;

import no.uis.service.studinfo.data.Vurdenhet;

public class VurdenhetConverter extends AbstractStringConverter<Vurdenhet> {

  @Override
  protected String convert(Vurdenhet v) {
    StringBuilder sb = new StringBuilder();
    if (v.isSetVurdstatus()) {
      sb.append(v.getVurdstatus());
      sb.append(": "); //$NON-NLS-1$
    }
    sb.append(v.getTidReell().getMonth());
    sb.append('/');
    sb.append(v.getTidReell().getYear());
    return sb.toString();
  }

}
