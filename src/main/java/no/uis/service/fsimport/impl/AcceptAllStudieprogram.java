package no.uis.service.fsimport.impl;

import no.uis.service.studinfo.data.Studieprogram;


public class AcceptAllStudieprogram implements StudinfoFilter<Studieprogram> {

  @Override
  public boolean accept(Studieprogram elem) {
    return true;
  }

}
