package no.uis.service.component.studinfopdf.convert;

import no.uis.service.studinfo.data.InngarIStudieprogram;

public class InngarIStudieprogramConverter extends AbstractStringConverter<InngarIStudieprogram> {

  @Override
  protected String convert(InngarIStudieprogram value) {
    if (value.isSetStudieprogramnavn()) {
      return value.getStudieprogramnavn();
    }
    
    return value.getStudieprogramkode();
  }

}
