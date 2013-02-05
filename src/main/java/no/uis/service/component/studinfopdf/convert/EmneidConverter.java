package no.uis.service.component.studinfopdf.convert;

import no.uis.service.studinfo.data.Emneid;

public class EmneidConverter extends AbstractStringConverter<Emneid> {

  @Override
  protected String convert(Emneid value) {
    StringBuilder sb = new StringBuilder();
//    sb.append(value.getInstitusjonsnr().toString());
//    sb.append('_');
    sb.append(value.getEmnekode());
    sb.append('_');
    sb.append(value.getVersjonskode());
    return sb.toString();
  }
}
