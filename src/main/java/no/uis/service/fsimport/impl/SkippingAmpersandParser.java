package no.uis.service.fsimport.impl;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.XIncludeAwareParserConfiguration;
import org.xml.sax.SAXException;

public class SkippingAmpersandParser extends SAXParser {

  public SkippingAmpersandParser() throws SAXException {
    super(new ParserConfig());
  }

  private static class ParserConfig extends XIncludeAwareParserConfiguration {

    @SuppressWarnings("unchecked")
    public ParserConfig() {
      super();
      fEntityManager = new EntityManager();
      fProperties.put(ENTITY_MANAGER, fEntityManager);
      addCommonComponent(fEntityManager);
    }

  }
}
