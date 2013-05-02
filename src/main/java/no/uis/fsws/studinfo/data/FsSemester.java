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

package no.uis.fsws.studinfo.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import no.uis.fsws.studinfo.util.FsSemesterAdapter;

/**
 * Serialized object for 'H&Oslash;ST' / 'V&Aring;R'.
 * The order of the elements must be chronological.
 */
@XmlType(name = "FsSemester" , namespace = "http://fsws.usit.no/schemas/studinfo")
@XmlEnum
@XmlJavaTypeAdapter(value = FsSemesterAdapter.class)
public enum FsSemester {

  /**
   * V&Aring;R.
   */
  VAR("V\u00c5R"),
  
  /**
   * H&Oslash;ST.
   */
  HOST("H\u00d8ST");
  
  private final String val;
  
  /**
   * There are problems with charatersets when converting from string and the platform encoding doesn't match the editor encoding.
   * @param str
   * @param matchString
   */
  FsSemester(String str) {
    this.val = str;
  }
  
  /**
   * Convert a string to {@link FsSemester}.
   * The string can be given as the {@link FsSemester#name()} or the {@link #val} property.
   * 
   * @param str
   * @return
   */
  public static FsSemester stringToUisSemester(String str) {
    for (FsSemester sem : FsSemester.values()) {
      if (sem.val.equals(str) || sem.name().equals(str)) {
        return sem;
      }
    }
    throw new IllegalArgumentException(str);
  }
  
  @Override
  public String toString() {
    return val;
  }
}
