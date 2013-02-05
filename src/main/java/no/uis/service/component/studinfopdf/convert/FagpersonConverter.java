package no.uis.service.component.studinfopdf.convert;

import no.uis.service.studinfo.data.Fagperson;

public class FagpersonConverter extends AbstractStringConverter<Fagperson> {

  @Override
  protected String convert(Fagperson value) {
    StringBuilder sb = new StringBuilder();
    sb.append(value.getPersonnavn().getFornavn());
    sb.append(' ');
    sb.append(value.getPersonnavn().getEtternavn());
    sb.append(" ("); //$NON-NLS-1$
    sb.append(value.getPersonrolle());
    sb.append(')');
    return sb.toString();
  }

}
