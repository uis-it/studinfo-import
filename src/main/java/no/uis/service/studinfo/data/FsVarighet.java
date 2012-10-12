package no.uis.service.studinfo.data;

public class FsVarighet {

  private static final String AAR = new String(new char[] {'\u00e5', 'r'});

  public static enum Unit {
    YEAR,
    SEMESTER,
    DAY,
    HOUR,
    MINUTE;
  }
  
  private int number;
  private Unit unit;
  
  public FsVarighet(int number, Unit unit) {
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
  
      int number = Integer.parseInt(varig[0]);
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
        
        case "semestre":
        case "semesters":
          unit = Unit.SEMESTER;
          break;
          
        case "dager":
        case "days":
          unit = Unit.DAY;
          break;
          
        case "timer":
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
    switch (unit)
      {
        case YEAR:
          return number * 2;

        case SEMESTER:
          return number;
      }
    return 0;
  }

  public int getNumber() {
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
