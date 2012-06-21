package no.uis.service.fsimport.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import no.uis.service.studinfo.data.FsTerminkodeYearMonth;

public class FsTerminkodeYMAdapter extends XmlAdapter<FsTerminkodeYearMonth, String> {

  @Override
  public String unmarshal(FsTerminkodeYearMonth v) throws Exception {
    return v.toString();
  }

  @Override
  public FsTerminkodeYearMonth marshal(String v) throws Exception {
    return FsTerminkodeYearMonth.valueOf(v);
  }
}
