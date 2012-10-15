package no.uis.service.studinfo.data;

/**
 * Represents year and semester 
 */
public class FsSemesterkode {

  private final int year;
  private final FsSemester semester;

  public FsSemesterkode(int year, FsSemester semester) {
    
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
}
