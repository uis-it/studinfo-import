package no.uis.service.fsimport.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import no.uis.service.studinfo.data.FsVarighet;

public class FsVarighetAdapter extends XmlAdapter<String, FsVarighet> {

  @Override
  public FsVarighet unmarshal(String v) throws Exception {
    FsVarighet varig = FsVarighet.valueOf(v);
    return varig;
  }

  @Override
  public String marshal(FsVarighet v) throws Exception {
    return v.toString();
  }

}
