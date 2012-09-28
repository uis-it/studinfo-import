package no.uis.service.studinfo.data;

public class FsVarighet {

  private static final String AAR = "" + '\u00c5'+"R";

  public static enum Unit {
    SEMESTER,
    YEAR;
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
      String sUnit = varig[1].toUpperCase();
      
      Unit unit = null;
      if (sUnit.startsWith("SEMEST")) {
        unit = Unit.SEMESTER;
      } else if (sUnit.startsWith("YEAR") || sUnit.equals(AAR)) {
        unit = Unit.YEAR;
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
