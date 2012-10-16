package no.uis.service.fsimport.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import no.uis.service.studinfo.data.FsYearSemester;

public class FsYearSemesterAdapter extends XmlAdapter<String, FsYearSemester> {

  @Override
  public FsYearSemester unmarshal(String v) throws Exception {
    return FsYearSemester.valueOf(v);
  }

  @Override
  public String marshal(FsYearSemester v) throws Exception {
    return v.toString();
  }
}
