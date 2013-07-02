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

package no.uis.fsws.studinfo.data;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Value;

import no.uis.fsws.studinfo.util.CalendarAdapter;
import no.uis.fsws.studinfo.util.CalendarNorwegianAdapter;
import no.uis.fsws.studinfo.util.FsTimeAdapter;

/**
 * Tries to represent a exam date.
 * @see #PATTERN
 */
@RequiredArgsConstructor
@Value public class FsEksamensdato {

  // taken from the XSD restriction
  private static final Pattern PATTERN = 
      Pattern.compile("("
          + "(Uttak\\: \\d{2}\\.\\d{2}\\.\\d{4} )?"
          + "(Frist innlevering\\: )?"
          + "(\\d{2}\\.\\d{2}\\.\\d{4})( kl\\. \\d{2}\\:\\d{2})?"
          + ")?");
  private static final CalendarAdapter DATE_ADAPTER = new CalendarNorwegianAdapter();
  private static final FsTimeAdapter TIME_ADAPTER = new FsTimeAdapter();

  Calendar dato;
  Calendar uttak;
  Calendar innleveringDato;
  FsTime innleveringTid;
  String text;

  @Override
  public String toString() {
    return text;
  }

  public static FsEksamensdato valueOf(String v) throws Exception {
    Matcher m = PATTERN.matcher(v);

    if (!m.matches()) {
      throw new IllegalArgumentException(v);
    }
    int nboGroups = m.groupCount();
    String[] groups = new String[nboGroups];

    int nonZero = 0;
    for (int i = 0; i < nboGroups; i++) {
      groups[i] = m.group(i + 1);
      if (groups[i] != null) {
        nonZero++;
      }
    }
    Calendar dato = null;
    Calendar uttak = null;
    Calendar innleveringDato = null;
    FsTime innleveringTid = null;

    // CHECKSTYLE:OFF
    switch (nonZero) {
      case 2:
        // 01.08.2012
        dato = DATE_ADAPTER.unmarshal(groups[3]);
        break;
      case 3:
        // Frist innlevering: 12.04.2012
        innleveringDato = DATE_ADAPTER.unmarshal(groups[3]);
        break;
      case 4:
        // Frist innlevering: 01.06.2012 kl. 14:00
        innleveringDato = DATE_ADAPTER.unmarshal(groups[3]);
        innleveringTid = TIME_ADAPTER.unmarshal(groups[4].substring(4).trim());
        break;
      case 5:
        // Uttak: 11.05.2012 Frist innlevering: 15.05.2012 kl. 14:00
        uttak = DATE_ADAPTER.unmarshal(groups[1].substring(6).trim());
        innleveringDato = DATE_ADAPTER.unmarshal(groups[3].trim());
        innleveringTid = TIME_ADAPTER.unmarshal(groups[4].substring(4).trim());
        break;

      default:
        break;
    }
    // CHECKSTYLE:ON

    return new FsEksamensdato(dato, uttak, innleveringDato, innleveringTid, v);
  }
}
