package no.uis.service.studinfo.data;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.uis.service.fsimport.util.CalendarAdapter;
import no.uis.service.fsimport.util.CalendarNorwegianAdapter;
import no.uis.service.fsimport.util.FsTimeAdapter;

public class FsEksamensdato {

  private final Calendar dato;
  private final Calendar uttak;
  private final Calendar innleveringDato;
  private final FsTime innleveringTid;
  
  // taken from the XSD restriction
  private static final Pattern PATTERN = Pattern.compile("((Uttak\\: \\d{2}\\.\\d{2}\\.\\d{4} )?(Frist innlevering\\: )?(\\d{2}\\.\\d{2}\\.\\d{4})( kl\\. \\d{2}\\:\\d{2})?)?");
  private static final CalendarAdapter dateAdapter = new CalendarNorwegianAdapter();
  private static final FsTimeAdapter timeAdapter = new FsTimeAdapter();
  
  public FsEksamensdato(Calendar dato, Calendar uttak, Calendar innleveringDato, FsTime innleveringTid) {
    this.dato = dato;
    this.uttak = uttak;
    this.innleveringDato = innleveringDato;
    this.innleveringTid = innleveringTid;
  }

  public Calendar getDato() {
    return dato;
  }

  public Calendar getUttak() {
    return uttak;
  }

  public Calendar getInnleveringDato() {
    return innleveringDato;
  }

  public FsTime getInnleveringTid() {
    return innleveringTid;
  }
  
  @Override
  public String toString() {
    
    try {
      if (dato != null) {
          return dateAdapter.marshal(dato);
      }
      StringBuilder sb = new StringBuilder();
      if (uttak != null) {
        sb.append("Uttak: ");
        sb.append(dateAdapter.marshal(uttak));
      }
      if (innleveringDato != null) {
        if (sb.length() > 0) {
          sb.append(' ');
        }
        sb.append("Frist innlevering: ");
        sb.append(dateAdapter.marshal(innleveringDato));
      }
      if (innleveringTid != null) {
        sb.append(" kl. ");
        sb.append(innleveringTid.toString());
      }
      return sb.toString();
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
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
    if (nonZero < 2 || nonZero > 5) {
      throw new IllegalArgumentException(v);
    }
    if (nonZero == 2) {
      // 01.08.2012
      dato = dateAdapter.unmarshal(groups[3]);
    } else {
      // TODO optimize
      if (nonZero == 3) {
        // Frist innlevering: 12.04.2012
        innleveringDato = dateAdapter.unmarshal(groups[3]);
      }
      if (nonZero == 4) {
        // Frist innlevering: 01.06.2012 kl. 14:00
        innleveringDato = dateAdapter.unmarshal(groups[3]);
        innleveringTid = timeAdapter.unmarshal(groups[4].substring(4).trim());
      }
      if (nonZero == 5) {
        // Uttak: 11.05.2012 Frist innlevering: 15.05.2012 kl. 14:00
        uttak = dateAdapter.unmarshal(groups[1].substring(6).trim());
        innleveringDato = dateAdapter.unmarshal(groups[3].trim());
        innleveringTid = timeAdapter.unmarshal(groups[4].substring(4).trim());
      }
    }
    return new FsEksamensdato(dato, uttak, innleveringDato, innleveringTid);
  }
}
