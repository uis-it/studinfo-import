package no.uis.service.fsimport.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import no.uis.service.studinfo.data.FsTerminkodeYearMonth;

public class FsTerminkodeYMAdapter extends XmlAdapter<String, FsTerminkodeYearMonth> {

  @Override
  public FsTerminkodeYearMonth unmarshal(String v) throws Exception {
    return FsTerminkodeYearMonth.valueOf(v);
  }

  @Override
  public String marshal(FsTerminkodeYearMonth v) throws Exception {
    return v.toString();
  }
}
