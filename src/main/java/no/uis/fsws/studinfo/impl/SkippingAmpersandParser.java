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

package no.uis.fsws.studinfo.impl;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.XIncludeAwareParserConfiguration;
import org.xml.sax.SAXException;

/**
 * This class is instantiated by Saxon in {@link AbstractStudinfoImport} via its class name.
 * The class name is set in the Spring config file.
 *
 */
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
