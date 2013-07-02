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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Value;

/**
 * Represents year and semester.
 */
@RequiredArgsConstructor
@Value public class FsYearSemester {

  int year;
  @NonNull FsSemester semester;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(year);
    sb.append(semester.name().charAt(0));
    return super.toString();
  }

  public static FsYearSemester valueOf(final String val) {
    if (val == null) {
      return null;
    }
    String newVal = val.trim();
    if (newVal.isEmpty()) {
      return null;
    }
    
    // CHECKSTYLE:OFF
    if (newVal.length() != 5) {
      throw new IllegalArgumentException(newVal);
    }
    int year = Integer.parseInt(newVal.substring(0, 4));
    FsSemester semester = null;
    switch (newVal.charAt(4)) {
      case 'H':
        semester = FsSemester.HOST;
        break;
      case 'V':
        semester = FsSemester.VAR;
        break;
      default:
        throw new IllegalArgumentException(newVal);
    }
    // CHECKSTYLE:ON
    return new FsYearSemester(year, semester);
  }
}
