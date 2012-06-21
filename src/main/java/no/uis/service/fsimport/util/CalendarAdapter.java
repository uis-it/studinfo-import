package no.uis.service.fsimport.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;


public class CalendarAdapter extends XmlAdapter<String, Calendar> {

  private final DateFormat format;
  
  public CalendarAdapter(DateFormat df) {
    this.format = df;
  }
  
  @Override
  public Calendar unmarshal(String v) throws Exception {
    Date date = format.parse(v);
    Calendar cal = GregorianCalendar.getInstance();
    cal.set(date.getYear() + 1900, date.getMonth(), date.getDate());
    return cal;
  }

  @Override
  public String marshal(Calendar v) throws Exception {
    return format.format(v.getTime());
  }
}
