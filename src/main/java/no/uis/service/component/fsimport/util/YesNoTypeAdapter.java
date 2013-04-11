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

package no.uis.service.component.fsimport.util;

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
