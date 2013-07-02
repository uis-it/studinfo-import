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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Value;

/**
 * A time given as String 'HH:mm'.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FsTime {
  int minutes;
  int hours;
  
  @Override
  public String toString() {
    return String.format("%02d:%02d", hours, minutes);
  }
  
  /**
   * Converts a string of the form mm:hh into hour and minutes.
   * @param v
   * @return
   */
  public static FsTime valueOf(String v) {
    
    if (v == null || v.isEmpty()) {
      return null;
    }
    // CHECKSTYLE:OFF
    if (v.length() != 5) {
      throw new IllegalArgumentException(v);
    }
    String[] tokens = v.split(":");
    if (tokens.length != 2) {
      throw new IllegalArgumentException(v);
    }
    // CHECKSTYLE:ON
    int hh = Integer.parseInt(tokens[0]);
    int mm = Integer.parseInt(tokens[1]);
    
    if (isValidTime(hh, mm)) { 
      return new FsTime(hh, mm);
    } else {
      throw new IllegalArgumentException();
    }
  }
  
  private static boolean isValidTime(int hh, int mm) {
    if (hh < 0 || mm < 0) {
      return false;
    }
    // CHECKSTYLE:OFF
    // check for valid times
    long sum = hh * 60L + mm;
    if (sum > (24L * 60L)) {
      return false;
    }
    // CHECKSTYLE:ON
    return true;
  }
}
