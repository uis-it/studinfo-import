/*
 Copyright 2010-2013 University of Stavanger, Norway

 Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package no.uis.fsws.studinfo;

import no.uis.fsws.studinfo.data.FsStudieinfo;

/**
 * Interface for main service of this library.
 */
public interface StudInfoImport {

  /**
   * {@link Integer} value for 0.
   */
  Integer INTEGER_0 = Integer.valueOf(0);
  
  /**
   * {@link Integer} value for 1.
   */
  Integer INTEGER_1 = Integer.valueOf(1);
  
  /**
   * {@link Integer} value for -1.
   * -1 means all possible values, e.g. all faculties
   */
  Integer INTEGER_MINUS_1 = Integer.valueOf(-1);

  /**
   * Type of study information. 
   */
  public static enum StudinfoType {
    /**
     * Subjects/courses.
     */
    EMNE, 
    
    /**
     * Study programme.
     */
    STUDIEPROGRAM,
    
    /**
     * Post-graduate courses.
     */
    KURS
  }

  /**
   * @param institution
   *        - FS code for institution
   * @param faculty
   *        - FS code for faculty
   * @param year
   *        - four digits year
   * @param semester
   *        - H&Oslash;ST/V&Aring;R
   * @param includeEP
   *        - include Education Plan (Utdanningsplan)
   * @param language
   *        - one letter language code: (B)okm&aring;l, (N)ynorsk, (E)nglish
   * @throws Exception
   * 
   */
  FsStudieinfo fetchStudyPrograms(int institution, int faculty, int year, String semester, boolean includeEP,
      String language) throws Exception;

  /**
   * @param institution
   *        - FS code for institution
   * @param faculty
   *        - FS code for faculty
   * @param year
   *        - four digits year
   * @param semester
   *        - H&Oslash;ST/V&Aring;R
   * @param language
   *        - one letter language code: (B)okm&aring;l, (N)ynorsk, (E)nglish
   * @throws Exception
   */
  FsStudieinfo fetchSubjects(int institution, int faculty, int year, String semester, String language) throws Exception;

  /**
   * @param institution
   *        - 217 for UiS
   * @param year
   *        - four digits year
   * @param semester
   *        - H&Oslash;ST/V&Aring;R
   * @param language
   *        - one letter language code: (B)okm&aring;l, (N)ynorsk, (E)nglish
   * @throws Exception
   */
  FsStudieinfo fetchCourses(int institution, int year, String semester, String language) throws Exception;
}
