package no.uis.service.fsimport.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import no.uis.service.studinfo.data.FsTime;

public class FsTimeAdapter extends XmlAdapter<String, FsTime> {

  @Override
  public FsTime unmarshal(String v) throws Exception {
    return FsTime.valueOf(v);
  }

  @Override
  public String marshal(FsTime v) throws Exception {
    return v.toString();
  }
}
