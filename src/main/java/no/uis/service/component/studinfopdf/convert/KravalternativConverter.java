package no.uis.service.component.studinfopdf.convert;

import no.uis.service.studinfo.data.Kravalternativ;

public class KravalternativConverter extends AbstractStringConverter<Kravalternativ> {

  @Override
  protected String convert(Kravalternativ value) {
    return value.getContent();
  }
}
