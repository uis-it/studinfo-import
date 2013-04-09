package no.uis.service.studinfo.data;

/**
 * Represents year and semester.
 */
public class FsYearSemester {

  private final int year;
  private final FsSemester semester;

  public FsYearSemester(int year, FsSemester semester) {
    
    if (semester == null) {
      throw new IllegalArgumentException();
    }
    this.year = year;
    this.semester = semester; 
  }
  
  public int getYear() {
    return year;
  }

  public FsSemester getSemester() {
    return semester;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(year);
    sb.append(semester.name().charAt(0));
    return super.toString();
  }

  public static FsYearSemester valueOf(String v) {
    if (v == null) {
      return null;
    }
    v = v.trim();
    if (v.isEmpty()) {
      return null;
    }
    
    if (v.length() != 5) {
      throw new IllegalArgumentException(v);
    }
    int year = Integer.parseInt(v.substring(0, 4));
    FsSemester semester = null;
    switch (v.charAt(4)) {
      case 'H':
        semester = FsSemester.HOST;
        break;
      case 'V':
        semester = FsSemester.VAR;
        break;
      default:
        throw new IllegalArgumentException(v);
          
    }
    return new FsYearSemester(year, semester);
  }
}
