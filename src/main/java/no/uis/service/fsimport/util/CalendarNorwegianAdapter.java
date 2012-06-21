package no.uis.service.fsimport.util;

import java.text.SimpleDateFormat;

public class CalendarNorwegianAdapter extends CalendarAdapter {

  public CalendarNorwegianAdapter() {
    super(new SimpleDateFormat("dd.MM.yyyy"));
  }
}
