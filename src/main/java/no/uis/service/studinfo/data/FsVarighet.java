package no.uis.service.studinfo.data;

public class FsVarighet {

  private static final String AAR = new String(new char[] {'\u00e5', 'r'});

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
  
  public static FsVarighet valueOf(String v) {
    if (v == null || v.trim().isEmpty()) {
      return null;
    }
    
    String[] varig = v.trim().split("\\s+");
    if (varig.length == 2) {
  
      float number = Float.parseFloat(varig[0]);
      String sUnit = varig[1].toLowerCase();

      // fix for file incoding mixup
      if (AAR.equals(sUnit)) {
        sUnit = "år";
      }
      
      Unit unit;
      switch(sUnit) {
        case "år":
        case "years":
          unit = Unit.YEAR;
          break;
        
        case "semester":
        case "semestre":
        case "semesters":
          unit = Unit.SEMESTER;
          break;
        
        case "måned":
        case "måneder":
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
