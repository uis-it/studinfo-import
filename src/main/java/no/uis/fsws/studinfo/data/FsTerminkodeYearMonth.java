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

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Value;

/**
 * Represents a the string 'yyyyMMC'.
 * Used e.g. in 'Vurdkombinasjon/tid'. 
 * <br/>
 * 
 * yyyy - year<br/>
 * MM - month<br/>
 * C - a code
 */

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Value public class FsTerminkodeYearMonth implements Serializable {

  private static final long serialVersionUID = 1L;

  int year;
  int month;
  String code;
  
  @Override
  public String toString() {
    return String.format("%04d%02d%s", year, month, code);
  }
  
  public static FsTerminkodeYearMonth valueOf(String v) {
    if (v == null) {
      return null;
    }
    // CHECKSTYLE:OFF
    if (v.length() < 6 || v.length() > 8) {
      throw new IllegalArgumentException(v);
    }
    
    int year = Integer.parseInt(v.substring(0, 4));
    int month = Integer.parseInt(v.substring(4, 6));
    String code = "";
    if (v.length() > 6) {
      code = v.substring(6);
    }
    // CHECKSTYLE:ON
    
    return new FsTerminkodeYearMonth(year, month, code);
  }
}
