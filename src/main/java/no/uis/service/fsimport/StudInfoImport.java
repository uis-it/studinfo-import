package no.uis.service.fsimport;

import no.uis.service.model.ImportReport;

public interface StudInfoImport {

	public final static String PACKAGE_SUBJECT = "Emnebeskrivelse";
	public final static String PACKAGE_STUDY_PROGRAM = "Studieprogram";
	public final static String PACKAGE_EVU = "Etterutdanning";
	
	/**
	 * 
	 * @param institution - 217 for UiS
	 * @param year - four digits year
	 * @param semester - H&Oslash;ST/V&Aring;R
	 * @param includeEP - include Education Plan (Utdanningsplan)
	 * @param language - one letter language code: (B)okm&aring;l, (N)ynorsk, (E)nglish 
	 * @throws Exception
	 */
	public ImportReport importStudyPrograms(int institution, int year, String semester, boolean includeEP, String language) throws Exception;	
	
	public ImportReport importSubjects(int institution, int year, String semester, String language) throws Exception;

	public ImportReport importCourses(int institution, int year, String semester, String language, boolean cleanBeforeUpdate) throws Exception;
}
