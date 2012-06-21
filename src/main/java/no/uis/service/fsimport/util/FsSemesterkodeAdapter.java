package no.uis.service.fsimport.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import no.uis.service.studinfo.data.FsSemester;
import no.uis.service.studinfo.data.FsSemesterkode;

public class FsSemesterkodeAdapter extends XmlAdapter<String, FsSemesterkode> {

  @Override
  public FsSemesterkode unmarshal(String v) throws Exception {
    if (v == null || v.length() != 5) {
      return null;
    }
    int year = Integer.parseInt(v.substring(0, 4));
    String semesterChar = v.substring(4);
    FsSemester semester = null;
    if (semesterChar.equals("H")) {
      semester = FsSemester.HOST;
    } else if (semesterChar.equals("V")) {
      semester = FsSemester.VAR;
    }
    return new FsSemesterkode(year, semester);
  }

  @Override
  public String marshal(FsSemesterkode v) throws Exception {
    return v.toString();
  }
}
