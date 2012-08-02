package no.uis.service.fsimport;

import java.util.List;

import no.uis.service.studinfo.data.Emne;
import no.uis.service.studinfo.data.Kurs;
import no.uis.service.studinfo.data.Studieprogram;

public interface StudInfoImport {

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
