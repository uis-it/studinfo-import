package no.uis.service.fsimport.util;

import java.text.SimpleDateFormat;

public class CalendarJDBCAdapter extends CalendarAdapter {
  SimpleDateFormat df = new SimpleDateFormat("yyyy-dd-MM");

  public CalendarJDBCAdapter() {
    super(new SimpleDateFormat("yyyy-dd-MM"));
  }
}
