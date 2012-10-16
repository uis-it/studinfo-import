package no.uis.service.fsimport.util;

import java.util.Iterator;
import java.util.List;

import no.uis.service.studinfo.data.Emnekombinasjon;
import no.uis.service.studinfo.data.FsSemester;
import no.uis.service.studinfo.data.FsYearSemester;
import no.uis.service.studinfo.data.KravSammensetting;
import no.uis.service.studinfo.data.ProgramEmne;
import no.uis.service.studinfo.data.Utdanningsplan;
import no.uis.service.studinfo.data.Vurdordning;

public final class Studinfos {

  public static final String VALID_FROM = "validFrom";
  public static final String SKIP_SEMESTERS = "skipSemesters";
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Studinfos.class);
  
  private static ContextPath contextPath = new ContextPath();  
  
  private Studinfos() {
  }

  public static void cleanUtdanningsplan(String programCode, Utdanningsplan uplan, FsYearSemester currentSemester, int maxSemesters) {
    contextPath.init(programCode);
    try {
      if (uplan != null && uplan.isSetKravSammensetting()) {
        cleanKravSammensettings(uplan.getKravSammensetting(), currentSemester, maxSemesters);
      }
    } finally {
      contextPath.remove();
    }
  }
  
  /**
   * Calculating the difference between {@code ys1} and {@code ys2}.
   * Like ys1 - ys2.
   * @return the difference in semesters
   */
  public static int getDiffSemesters(FsYearSemester ys1, FsYearSemester ys2) {
    
    int diff = 2 * (ys1.getYear() - ys2.getYear());

    diff += (ys1.getSemester().ordinal() - ys2.getSemester().ordinal());

    return diff;
  }

  public static FsYearSemester getValidFrom(KravSammensetting ks) {
    int validFromYear = 0;
    FsSemester validFromSemester = null;
    if (ks.isSetArstallGjelderFra()) {
      validFromYear = ks.getArstallGjelderFra().getYear();
      validFromSemester = ks.getTerminkodeGjelderFra();
    } else if (ks.isSetTerminGjelderFra()) {
      String terminGjelderFra = ks.getTerminGjelderFra();
      try {
        validFromYear = Integer.parseInt(terminGjelderFra.substring(0, 4));
        char semesterChar = terminGjelderFra.charAt(4);
        validFromSemester = semesterChar == 'H' ? FsSemester.HOST : (semesterChar == 'V' ? FsSemester.VAR : null);
      } catch(Exception e) {
        log.warn(contextPath.getPath() + ": " + terminGjelderFra, e);
      }
    }
    return new FsYearSemester(validFromYear, validFromSemester);
  }
  

  private static void cleanKravSammensettings(List<KravSammensetting> kravSammensetting, FsYearSemester currentSemester, final int maxSemesters)
  {
    Iterator<KravSammensetting> iter = kravSammensetting.iterator();
    while (iter.hasNext()) {
      KravSammensetting kravSammen = iter.next();
      FsYearSemester yearSemester = getValidFrom(kravSammen);
      int skipSemesters = getDiffSemesters(currentSemester, yearSemester);
      
      kravSammen.addProperty(VALID_FROM, yearSemester);
      kravSammen.addProperty(SKIP_SEMESTERS, skipSemesters);
      
      boolean doRemove = maxSemesters <= skipSemesters;
      if (!doRemove) {
        doRemove = cleanKravSammensetting(kravSammen, maxSemesters, skipSemesters);
      } 
      if (doRemove) {
        iter.remove();
      }
    }
  }

  private static boolean cleanKravSammensetting(KravSammensetting kravSammen, int maxSemesters, int skipSemesters) {

    return cleanEmneKombinasjon(kravSammen.getEmnekombinasjon(), 0, skipSemesters) == 0;
  }

  /**
   * @return number of valid emner
   */
  private static int cleanEmneKombinasjon(Emnekombinasjon ek, int offset, final int skipSemesters) {

    int nboEmne = 0;
    contextPath.push(ek.getEmnekombinasjonskode());
    if (ek.isSetEmne()) {
      Iterator<ProgramEmne> iter = ek.getEmne().iterator();
      while (iter.hasNext()) {
        ProgramEmne emne = iter.next();
        contextPath.push(emne.getEmneid().getEmnekode());
        if (!isValidEmne(emne, offset, skipSemesters)) {
          iter.remove();
        }
        contextPath.pop();
      }
      nboEmne += ek.getEmne().size();
    }

    if (ek.isSetEmnekombinasjon()) {
      if (ek.isSetTerminnrRelativStart()) {
        offset += (ek.getTerminnrRelativStart().intValue() - 1);
      }
      for (Emnekombinasjon subEk : ek.getEmnekombinasjon()) {
        nboEmne += cleanEmneKombinasjon(subEk, offset, skipSemesters);
      }
    }
    
    contextPath.pop();
    return nboEmne;
  }
  
  private static boolean isValidEmne(ProgramEmne emne, int terminOffset, final int skipSemesters) {
    int ufra = emne.getUndterminFra().intValue();
    int util = emne.isSetUndterminTil() ? emne.getUndterminTil().intValue() : ufra;
    int udefault = emne.isSetUndterminDefault() ? emne.getUndterminDefault().intValue() : 0;

    if (udefault == 0) {
      udefault = ufra;
    }
    ufra += terminOffset;
    util += terminOffset;
    udefault += terminOffset;

    if (util <= skipSemesters) {
      return false;
    }
    return true;
  }

  public static void cleanVurderingsordning(List<Vurdordning> vurdordnings, List<String> excludeCodes, int year,
      FsSemester semester)
  {
    Iterator<Vurdordning> iter = vurdordnings.iterator();
    while (iter.hasNext()) {
      Vurdordning vo = iter.next();
      if (excludeCodes.contains(vo.getVurdordningid())) {
        iter.remove();
      } else {
        FsYearSemester sistegang = vo.getVurdkombinasjon().getSistegang();
        int skipSemesters = getDiffSemesters(new FsYearSemester(year, semester), sistegang);
        if (skipSemesters >= 0) {
          iter.remove();
        }
      }
    }
  }
  
  public static boolean isSetAndTrue(Boolean b) {
    if (b != null) {
      if (b.booleanValue()) {
        return true;
      }
    }
    return false;
  }
}
