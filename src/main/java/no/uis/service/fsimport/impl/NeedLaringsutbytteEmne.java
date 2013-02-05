package no.uis.service.fsimport.impl;

import no.uis.service.studinfo.data.Emne;

public class NeedLaringsutbytteEmne implements StudinfoFilter<Emne> {

  @Override
  public boolean accept(Emne emne) {
    return emne.isSetLaringsutbytte();
  }
}
