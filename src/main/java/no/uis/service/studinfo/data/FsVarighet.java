package no.uis.service.studinfo.data;

public class FsVarighet {

  public static enum UNIT {
    SEMESTER("Semestre"),
    YEAR("År");
    
    private String _name;

    private UNIT(String name) {
      this._name = name;
    }
    
    public String toString() {
      return this._name;
    }
  }
  
  private int number;
  private UNIT unit;
  
  public FsVarighet(int number, UNIT unit) {
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
    
    String[] varig = v.split("\\s+");
    if (varig.length == 2) {
  
      int number = Integer.parseInt(varig[0]);
      String sUnit = varig[1].toUpperCase();
      
      UNIT unit = null;
      if (sUnit.startsWith("SEMEST")) {
        unit = UNIT.SEMESTER;
      } else if (sUnit.equals("YEAR") || sUnit.equals("ÅR")) {
        unit = UNIT.YEAR;
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

  /**
   * This doesn't produce the English version.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    sb.append(number);
    sb.append(' ');
    sb.append(unit.toString());
    
    return sb.toString();
  }
}
