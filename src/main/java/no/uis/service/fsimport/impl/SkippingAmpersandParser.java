/*
 Copyright 2010-2013 University of Stavanger, Norway

 Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package no.uis.service.fsimport.impl;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.XIncludeAwareParserConfiguration;
import org.xml.sax.SAXException;

public class SkippingAmpersandParser extends SAXParser {

  public SkippingAmpersandParser() throws SAXException {
    super(new ParserConfig());
//    setProperty("http://apache.org/xml/properties/internal/entity-manager", new EntityManager(createEntities()));
//    setProperty("http://apache.org/xml/properties/internal/symbol-table", new SymbolTable());
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
