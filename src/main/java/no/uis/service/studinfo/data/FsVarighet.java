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

package no.uis.service.studinfo.data;

public class FsVarighet {

  /**
   * Durations in FS. 
   */
  public static enum Unit {
    YEAR,
    SEMESTER,
    MONTH,
    WEEK,
    DAY,
    HOUR,
    MINUTE;
  }
  
  private float number;
  private Unit unit;
  
  public FsVarighet(float number, Unit unit) {
    if (unit == null) {
      throw new IllegalArgumentException();
    }
    this.number = number;
    this.unit = unit;
  }
  
  /**
   * Parses a string to number and unit. 
   */
  public static FsVarighet valueOf(String v) {
    if (v == null || v.trim().isEmpty()) {
      return null;
    }
    
    String[] varig = v.trim().split("\\s+");
    if (varig.length == 2) {
  
      float number = Float.parseFloat(varig[0]);
      String sUnit = varig[1].toLowerCase();

      Unit unit;
      switch(sUnit) {
        case "\u00E5r":
        case "years":
          unit = Unit.YEAR;
          break;
        
        case "semester":
        case "semestre":
        case "semesters":
          unit = Unit.SEMESTER;
          break;
        
        case "m\u00E5ned":
        case "m\u00E5neder":
        case "month":
        case "months":
          unit = Unit.MONTH;
          break;
          
        case "uke":
        case "uker":
        case "week":
        case "weeks":
          unit = Unit.WEEK;
          break;
          
        case "dag":
        case "dager":
        case "day":
        case "days":
          unit = Unit.DAY;
          break;
          
        case "time":
        case "timer":
        case "hour":
        case "hours":
          unit = Unit.HOUR;
          break;
          
        case "minutter":
        case "minutes":
          unit = Unit.MINUTE;
          break;
        default:
          throw new IllegalArgumentException(sUnit);
      }
      
      return new FsVarighet(number, unit);
    } 
    
    throw new IllegalArgumentException(v);
  }

  public int getSemesters() {
    switch (unit) {
      case YEAR:
        return Math.round(number * 2.0f);

      case SEMESTER:
        return Math.round(number);
        
      default:
        throw new UnsupportedOperationException("Cannot convert " + unit.toString());
    }
  }

  public float getNumber() {
    return number;
  }

  public Unit getUnit() {
    return unit;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    sb.append(number);
    sb.append(' ');
    sb.append(unit.toString());
    
    return sb.toString();
  }
}
