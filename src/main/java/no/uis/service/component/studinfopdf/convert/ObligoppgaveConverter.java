package no.uis.service.component.studinfopdf.convert;

import no.uis.service.studinfo.data.Obligoppgave;

public class ObligoppgaveConverter extends AbstractStringConverter<Obligoppgave> {

  @Override
  protected String convert(Obligoppgave value) {
    StringBuilder sb = new StringBuilder();
    sb.append(value.getValue());
//    sb.append(" ("); //$NON-NLS-1$
//    sb.append(value.getNr());
//    sb.append(')');
    return sb.toString();
  }

}
