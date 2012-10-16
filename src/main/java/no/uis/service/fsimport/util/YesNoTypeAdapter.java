package no.uis.service.fsimport.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;


public class YesNoTypeAdapter extends XmlAdapter<String, Boolean> {

  @Override
  public Boolean unmarshal(String v) throws Exception {
    switch (v) {
      case "J":
        return Boolean.TRUE;
      case "N":
        return Boolean.FALSE;
      default:
        throw new IllegalArgumentException(v);
    }
  }

  @Override
  public String marshal(Boolean v) throws Exception {
    if (v.booleanValue()) {
      return "J"; //$NON-NLS-1$
    }
    return "N"; //$NON-NLS-1$ 
  }
}
