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
