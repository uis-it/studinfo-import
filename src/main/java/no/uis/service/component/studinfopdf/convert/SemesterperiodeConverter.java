package no.uis.service.component.studinfopdf.convert;

import no.uis.service.studinfo.data.Semesterperiode;

public class SemesterperiodeConverter extends AbstractStringConverter<Semesterperiode> {

  @Override
  protected String convert(Semesterperiode value) {
    StringBuilder sb = new StringBuilder();
    if (value.isSetForstegang()) {
      sb.append(value.getForstegang());
    }
    sb.append('-');
    if (value.isSetSistegang()) {
      sb.append(value.getSistegang());
    }
    return sb.toString();
  }
}
