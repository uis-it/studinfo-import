package no.uis.service.studinfo.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "FsSemester" , namespace = "http://fsws.usit.no/schemas/studinfo")
@XmlEnum
public enum FsSemester {

  // The order of these fields is significant, the fields must be sorted in chronological order 
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
  
  public static FsSemester stringToUisSemester(String str) {
    for (FsSemester sem : FsSemester.values()) {
      if (sem.val.equals(str)) {
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
