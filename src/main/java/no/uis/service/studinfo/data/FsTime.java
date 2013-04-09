package no.uis.service.studinfo.data;

public class FsTime {
  private final int minutes;
  private final int hours;
  
  public FsTime(int hours, int minutes) {
    if (!isValidTime(hours, minutes)) {
      throw new IllegalArgumentException();
    }
    this.hours = hours;
    this.minutes = minutes;
  }

  public int getMinutes() {
    return minutes;
  }

  public int getHours() {
    return hours;
  }
  
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
    if (v.length() != 5) {
      throw new IllegalArgumentException(v);
    }
    String[] tokens = v.split(":");
    if (tokens.length != 2) {
      throw new IllegalArgumentException(v);
    }
    int hh = Integer.parseInt(tokens[0]);
    int mm = Integer.parseInt(tokens[1]);

    return new FsTime(hh, mm);
  }
  
  private static boolean isValidTime(int hh, int mm) {
    if (hh < 0 || mm < 0) {
      return false;
    }
    // check for valid times
    long sum = hh * 60L + mm;
    if (sum > (24L * 60)) {
      return false;
    }
    return true;
  }
}
