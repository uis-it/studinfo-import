package no.uis.service.component.studinfopdf.convert;

import no.uis.service.fsimport.util.CalendarAdapter;
import no.uis.service.fsimport.util.CalendarNorwegianAdapter;
import no.uis.service.studinfo.data.Datoperiode;

public class DatoperiodeConverter extends AbstractStringConverter<Datoperiode> {

  private CalendarAdapter calendarAdapter = new CalendarNorwegianAdapter();

  @Override
  protected String convert(Datoperiode value) {
    StringBuilder sb = new StringBuilder();
    try {
      if (value.isSetFradato()) {
        sb.append(calendarAdapter.marshal(value.getFradato()));
      }
      sb.append(" - "); //$NON-NLS-1$
      if (value.isSetTildato()) {
        sb.append(calendarAdapter.marshal(value.getTildato()));
      }
    } catch(Exception e) {
      sb.append(e.getLocalizedMessage());
    }
    return sb.toString();
  }
}
