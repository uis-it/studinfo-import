package no.uis.service.fsimport.util;

import java.util.Iterator;
import java.util.List;

import no.uis.service.studinfo.data.Emnekombinasjon;
import no.uis.service.studinfo.data.FsSemester;
import no.uis.service.studinfo.data.FsSemesterkode;
import no.uis.service.studinfo.data.KravSammensetting;
import no.uis.service.studinfo.data.ProgramEmne;
import no.uis.service.studinfo.data.Utdanningsplan;

public final class Studinfos {

  public static final String SEMESTERKODE = "semesterkode";
  public static final String SKIP_SEMESTERS = "skipSemesters";
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Studinfos.class);
  
  private Studinfos() {
  }

  public static void cleanUtdanningsplan(Utdanningsplan uplan, int currentYear, FsSemester currentSemester, int maxSemesters) {
    if (uplan != null && uplan.isSetKravSammensetting()) {
      cleanKravSammensettings(uplan.getKravSammensetting(), currentYear, currentSemester, maxSemesters);
    }
  }
  
  /**
   * We want only current and future subjects (emner), older ones are skipped.
   */
  public static int getSkipSemesters(int catalogYear, FsSemester catalogSemester, FsSemesterkode semesterkode) {
    
    if (!semesterkode.isValid()) {
      log.warn("cannot determine validity of 'KravSammensetting'");
      return 0;
    }

    int skipSemesters = 2 * (catalogYear - semesterkode.getYear());

    skipSemesters += (catalogSemester.ordinal() - semesterkode.getSemester().ordinal());

    return skipSemesters;
  }

  public static FsSemesterkode getSemesterkode(KravSammensetting ks) {
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
        log.warn(terminGjelderFra, e);
      }
    }
    return new FsSemesterkode(validFromYear, validFromSemester);
  }
  

  private static void cleanKravSammensettings(List<KravSammensetting> kravSammensetting, final int currentYear,
      final FsSemester currentSemester, final int maxSemesters)
  {
    Iterator<KravSammensetting> iter = kravSammensetting.iterator();
    while (iter.hasNext()) {
      KravSammensetting kravSammen = iter.next();
      
      FsSemesterkode semesterkode = getSemesterkode(kravSammen);
      int skipSemesters = getSkipSemesters(currentYear, currentSemester, semesterkode);
      
      kravSammen.addProperty(SEMESTERKODE, semesterkode);
      kravSammen.addProperty(SKIP_SEMESTERS, skipSemesters);
      
      //maxSemesters -= skipSemesters;
      boolean doRemove = maxSemesters <= skipSemesters;
      if (!doRemove) {
        doRemove = cleanKravSammensetting(kravSammen, currentYear, currentSemester, maxSemesters, skipSemesters);
      } 
      if (doRemove) {
        iter.remove();
      }
    }
  }

  private static boolean cleanKravSammensetting(KravSammensetting kravSammen, int currentYear, FsSemester currentSemester, int maxSemesters, int skipSemesters) {

    return cleanEmneKombinasjon(kravSammen.getEmnekombinasjon(), 0, skipSemesters) == 0;
  }

  /**
   * @return number of valid emner
   */
  private static int cleanEmneKombinasjon(Emnekombinasjon ek, int offset, final int skipSemesters) {

    int nboEmne = 0;
    if (ek.isSetEmne()) {
      Iterator<ProgramEmne> iter = ek.getEmne().iterator();
      while (iter.hasNext()) {
        ProgramEmne emne = iter.next();
        if (!isValidEmne(emne, offset, skipSemesters)) {
          iter.remove();
        }
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
}
