package no.uis.service.studinfo.data;

import java.io.Serializable;

public class FsTerminkodeYearMonth implements Serializable {

  private static final long serialVersionUID = 1L;

  private final int year;
  private final int month;
  private final String code;
  
  private FsTerminkodeYearMonth(int year, int month, String code) {
    this.year = year;
    this.month = month;
    this.code = code;
  }

  @Override
  public String toString() {
    return String.format("%04d%02d%s", year, month, code);
  }
  
  public static FsTerminkodeYearMonth valueOf(String v) {
    if (v == null) {
      return null;
    }
    if (v.length() < 6 || v.length() > 8) {
      throw new IllegalArgumentException(v);
    }
    
    int year = Integer.parseInt(v.substring(0, 4));
    int month = Integer.parseInt(v.substring(4, 6));
    String code = "";
    if (v.length() > 6) {
      code = v.substring(6);
    }
    
    return new FsTerminkodeYearMonth(year, month, code);
  }
}
