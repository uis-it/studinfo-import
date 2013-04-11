/*
 Copyright 2010-2013 University of Stavanger, Norway

 Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package no.uis.service.component.fsimport.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;


public class CalendarAdapter extends XmlAdapter<String, Calendar> {

  private final DateFormat format;
  
  public CalendarAdapter(DateFormat df) {
    this.format = df;
  }
  
  @SuppressWarnings("deprecation")
  @Override
  public Calendar unmarshal(String v) throws ParseException {
    Date date = format.parse(v);
    Calendar cal = GregorianCalendar.getInstance();
    cal.clear();
    cal.set(date.getYear() + 1900, date.getMonth(), date.getDate());
    return cal;
  }

  @Override
  public String marshal(Calendar v) {
    return format.format(v.getTime());
  }
}
