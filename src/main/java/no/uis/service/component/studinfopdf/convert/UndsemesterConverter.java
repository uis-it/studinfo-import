package no.uis.service.component.studinfopdf.convert;

import java.util.List;

import no.uis.service.studinfo.data.Semester;
import no.uis.service.studinfo.data.Undsemester;

public class UndsemesterConverter extends AbstractStringConverter<Undsemester> {

  @Override
  protected String convert(Undsemester value) {
    List<Semester> semesterList = value.getSemester();
    boolean includeNumber = (semesterList.size() > 1);
    
    StringBuilder sb = new StringBuilder();
    
    for (Semester semester : semesterList) {
      if (sb.length() > 0) {
        sb.append(", "); //$NON-NLS-1$
      }
      if (includeNumber) {
        sb.append(semester.getNr());
        sb.append(':');
      }
      sb.append(semester.getValue());
    }
    boolean needHyphen = true;
    boolean hasValidPeriod = (value.isSetForstegang() || value.isSetSistegang());
    if (hasValidPeriod) {
      sb.append('(');
    }
    if (value.isSetForstegang()) {
      sb.append(value.getForstegang());
      sb.append('-');
      needHyphen = false;
    }
    if (value.isSetSistegang()) {
      if (needHyphen) {
        sb.append('-');
      }
      sb.append(value.getSistegang());
    }
    if (hasValidPeriod) {
      sb.append(')');
    }
    
    return sb.toString();
  }
}
