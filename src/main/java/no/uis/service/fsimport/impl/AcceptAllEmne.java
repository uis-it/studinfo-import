package no.uis.service.fsimport.impl;

import no.uis.service.studinfo.data.Emne;

public class AcceptAllEmne implements StudinfoFilter<Emne> {

  @Override
  public boolean accept(Emne elem) {
    return true;
  }

}
