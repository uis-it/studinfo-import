package no.uis.service.fsimport.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import no.uis.service.studinfo.data.FsEksamensdato;

public class FsEksamensdatoAdapter extends XmlAdapter<String, FsEksamensdato> {

  @Override
  public FsEksamensdato unmarshal(String v) throws Exception {
    return FsEksamensdato.valueOf(v);
  }

  @Override
  public String marshal(FsEksamensdato v) throws Exception {
    return v.toString();
  }
}
