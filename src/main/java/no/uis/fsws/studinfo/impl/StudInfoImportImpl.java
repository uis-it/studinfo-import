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

package no.uis.fsws.studinfo.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import no.usit.fsws.studinfo.StudInfoService;

import org.apache.cxf.helpers.IOUtils;

/**
 * Implementation that fetches the data, transforms it (if a transformer is given) and de-serializes it to a Java object tree.
 */
@Log4j
public class StudInfoImportImpl extends AbstractStudinfoImport {

  private static final String COPY_PREFIX_EMNE = "studinfo-emne-";

  private static final String COPY_PREFIX_PROGRAM = "studinfo-program-";

  @Setter private StudInfoService fsServiceStudInfo;
  @Setter private boolean copyXML;

  @Override
  protected Reader fsGetStudieprogram(int institution, int faculty, int year, String semester, boolean includeEP, String language)
  {
    Integer medUPinfo = includeEP ? INTEGER_1 : INTEGER_0;
    Integer iFaculty;
    if (faculty == -1) {
      iFaculty = INTEGER_MINUS_1;
    } else {
      iFaculty = Integer.valueOf(faculty);
    }
    String studieinfoXml = fsServiceStudInfo.getStudieprogramSI(year, semester, medUPinfo, null, institution, iFaculty,
      INTEGER_MINUS_1, null, language);
    
    if (copyXML) {
      copyXML(COPY_PREFIX_PROGRAM, studieinfoXml);
    }
    return new StringReader(studieinfoXml);
  }

  @Override
  protected Reader fsGetStudieprogram(String studieprogramkode, int year, String semester, boolean includeEP, String language) {
    Integer medUPinfo = includeEP ? INTEGER_1 : INTEGER_0;
    String studieinfoXml = fsServiceStudInfo.getStudieprogramSI(year, semester, medUPinfo, studieprogramkode, 
      INTEGER_MINUS_1, INTEGER_MINUS_1, INTEGER_MINUS_1, null, language);
    if (copyXML) {
      copyXML(COPY_PREFIX_PROGRAM, studieinfoXml);
    }
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
    String studieinfoXml = fsServiceStudInfo.getEmneSI(institution, null, null, iFaculty, INTEGER_MINUS_1, null, year, semester,
      language);
    
    if (copyXML) {
      copyXML(COPY_PREFIX_EMNE, studieinfoXml);
    }
    return new StringReader(studieinfoXml);
  }

  @Override
  protected Reader fsGetEmne(int institution, String emnekode, String versjonskode, int year, String semester, String language) {
    String studieinfoXml = fsServiceStudInfo.getEmneSI(institution, emnekode, versjonskode, INTEGER_MINUS_1, INTEGER_MINUS_1, 
      null, year, semester, language);
    
    if (copyXML) {
      copyXML(COPY_PREFIX_EMNE, studieinfoXml);
    }
    return new StringReader(studieinfoXml);
  }

  @Override
  protected Reader fsGetKurs(int institution, String language) {
    String studieinfoXml = fsServiceStudInfo.getKursSI(institution, INTEGER_MINUS_1, INTEGER_MINUS_1, INTEGER_MINUS_1, language);

    if (copyXML) {
      copyXML("studinfo-kurs-", studieinfoXml);
    }
    return new StringReader(studieinfoXml);
  }

  private void copyXML(String prefix, String xml) {
    try {
      File temp = File.createTempFile(prefix, ".xml");
      log.info("Copy XML to " + temp.getAbsolutePath());
      
      try (FileWriter output = new FileWriter(temp)) {
        IOUtils.copyAndCloseInput(new StringReader(xml), output);
      }
    } catch(IOException e) {
      log.warn(null, e);
    }
  }
}
