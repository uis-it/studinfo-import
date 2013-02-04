package no.uis.service.studinfo.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import no.uis.service.fsimport.util.FsSemesterAdapter;

@XmlType(name = "FsSemester" , namespace = "http://fsws.usit.no/schemas/studinfo")
@XmlEnum
@XmlJavaTypeAdapter(value=FsSemesterAdapter.class)
public enum FsSemester {

  // The order of these fields is significant, the fields must be sorted in chronological order
  // This cryptic string is necessary to cope with inconsistent file encodings
  VAR("V"+'\u00c5'+"R"),
  HOST("H"+'\u00d8' +"ST");
  
  private final String val;
  
  /**
   * There are problems with charatersets when converting from string and the platform encoding doesn't match the editor encoding.
   * @param str
   * @param matchString
   */
  FsSemester(String str) {
    this.val = str;
  }
  
  /**
   * Convert a string to {@link FsSemester}.
   * The string can be given as the {@link FsSemester#name()} or the {@link #val} property.
   * 
   * @param str
   * @return
   */
  public static FsSemester stringToUisSemester(String str) {
    for (FsSemester sem : FsSemester.values()) {
      if (sem.val.equals(str) || sem.name().equals(str)) {
        return sem;
      }
    }
    throw new IllegalArgumentException(str);
  }
  
  @Override
  public String toString() {
    return val;
  }
}
