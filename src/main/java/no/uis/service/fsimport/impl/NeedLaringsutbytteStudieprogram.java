package no.uis.service.fsimport.impl;

import no.uis.service.studinfo.data.Studieprogram;

public class NeedLaringsutbytteStudieprogram implements StudinfoFilter<Studieprogram> {

  @Override
  public boolean accept(Studieprogram prog) {
    return prog.isSetLaringsutbytte();
  }
}
