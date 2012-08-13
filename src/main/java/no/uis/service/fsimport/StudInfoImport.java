package no.uis.service.fsimport;

import java.util.List;

import no.uis.service.studinfo.data.Emne;
import no.uis.service.studinfo.data.Kurs;
import no.uis.service.studinfo.data.Studieprogram;

public interface StudInfoImport {

	public static final Integer INTEGER_0 = Integer.valueOf(0);
  public static final Integer INTEGER_1 = Integer.valueOf(1);
  /**
   * -1 means all possible values, e.g. all faculties
   */
  public static final Integer INTEGER_MINUS_1 = Integer.valueOf(-1);
  
  public enum StudinfoType {
    EMNE,
    STUDIEPROGRAM,
    KURS
  }

  /**
	 * 
	 * @param institution - 217 for UiS
	 * @param year - four digits year
	 * @param semester - H&Oslash;ST/V&Aring;R
	 * @param includeEP - include Education Plan (Utdanningsplan)
	 * @param language - one letter language code: (B)okm&aring;l, (N)ynorsk, (E)nglish 
	 * @throws Exception
	 */
	public List<Studieprogram> fetchStudyPrograms(int institution, int year, String semester, boolean includeEP, String language) throws Exception;	
	
	public List<Emne> fetchSubjects(int institution, int year, String semester, String language) throws Exception;

	public List<Kurs> fetchCourses(int institution, int year, String semester, String language) throws Exception;
}
