package no.uis.service.component.studinfopdf.convert;

import no.uis.service.studinfo.data.Sted;

public class StedConverter extends AbstractStringConverter<Sted> {

  @Override
  protected String convert(Sted value) {
    StringBuilder sb = new StringBuilder();
    if (value.getAvdnavn() != null) {
      sb.append(value.getAvdnavn());
    }
    if (value.getNavn() != null) {
      if (sb.length() > 0) {
        sb.append(", "); //$NON-NLS-1$
      }
      sb.append(value.getNavn());
    }
    return sb.toString();
  }
}
