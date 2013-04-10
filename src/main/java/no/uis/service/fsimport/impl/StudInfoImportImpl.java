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

package no.uis.service.fsimport.impl;

import java.io.Reader;
import java.io.StringReader;

import no.usit.fsws.wsdl.studinfo.StudInfoService;

public class StudInfoImportImpl extends AbstractStudinfoImport {

  private StudInfoService fsServiceStudInfo;
  public void setFsServiceStudInfo(StudInfoService fsServiceStudInfo) {
    this.fsServiceStudInfo = fsServiceStudInfo;
  }

  public StudInfoService getFsServiceStudInfo() {
    return this.fsServiceStudInfo;
  }

  @Override
  protected Reader fsGetStudieprogram(int institution, int faculty, int year, String semester, boolean includeEP, String language) {
    Integer medUPinfo = includeEP ? INTEGER_1 : INTEGER_0;
    Integer iFaculty;
    if (faculty == -1) {
      iFaculty = INTEGER_MINUS_1;
    } else {
      iFaculty = Integer.valueOf(faculty);
    }
    String studieinfoXml = fsServiceStudInfo.getStudieprogramSI(year, semester, medUPinfo, null, institution, iFaculty, INTEGER_MINUS_1, null, language);
    return new StringReader(studieinfoXml);
  }
  
  @Override
  protected Reader fsGetEmne(int institution, int faculty, int year, String semester, String language) {
    Integer iFaculty;
    if (faculty == -1) {
      iFaculty = INTEGER_MINUS_1;
    } else {
      iFaculty = Integer.valueOf(faculty);
    }
    String studieinfoXml = fsServiceStudInfo.getEmneSI(institution, null, null, iFaculty, INTEGER_MINUS_1, null, year, semester, language);
    return new StringReader(studieinfoXml);
  }
  
  @Override
  protected Reader fsGetKurs(int institution, String language) {
    String studieinfoXml = fsServiceStudInfo.getKursSI(institution, INTEGER_MINUS_1, INTEGER_MINUS_1, INTEGER_MINUS_1, language);
    return new StringReader(studieinfoXml);
  }
}
