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
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import net.sf.saxon.lib.FeatureKeys;
import no.uis.fsws.studinfo.StudInfoImport;
import no.uis.fsws.studinfo.data.FsStudieinfo;
import no.usit.fsws.studinfo.StudInfoService;

import org.apache.cxf.helpers.IOUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation that fetches the data, transforms it (if a transformer is given) and de-serializes it to a Java object tree.
 */
@Log4j
public class StudInfoImportImpl implements StudInfoImport {

  private static final String COPY_PREFIX_EMNE = "studinfo-emne-";

  private static final String COPY_PREFIX_PROGRAM = "studinfo-program-";

  @Setter(onMethod = @_(@Required)) @NonNull private URL transformerUrl;
  @Setter private String xmlSourceParser;
  @Setter private StudInfoService fsServiceStudInfo;
  @Setter private boolean copyXML;

  @Override
  public FsStudieinfo fetchStudyPrograms(int institution, int faculty, int year, String semester, boolean includeEP,
      String language) throws Exception
  {

    @Cleanup Reader studieinfoXml = fsGetStudieprogram(institution, faculty, year, semester, includeEP, language);
    return unmarshalStudieinfo(studieinfoXml);
  }

  @Override
  public FsStudieinfo fetchStudyProgram(String studieprogramkode, int year, String semester, boolean includeEP,
      String language) throws Exception
  {

    @Cleanup Reader studieinfoXml = fsGetStudieprogram(studieprogramkode, year, semester, includeEP, language);
    return unmarshalStudieinfo(studieinfoXml);
  }

  @Override
  public FsStudieinfo fetchSubjects(int institution, int faculty, int year, String semester, String language) throws Exception {

    @Cleanup Reader studieinfoXml = fsGetEmne(institution, faculty, year, semester, language);
    return unmarshalStudieinfo(studieinfoXml);
  }

  @Override
  public FsStudieinfo fetchSubject(int institusjonsnr, String emnekode, String versjonskode, int arstall, String terminkode,
      String sprak) throws Exception
  {
    @Cleanup Reader studieinfoXml = fsGetEmne(institusjonsnr, emnekode, versjonskode, arstall, terminkode, sprak);
    return unmarshalStudieinfo(studieinfoXml);
  }
  
  @Override
  public FsStudieinfo fetchCourses(int institution, int year, String semester, String language) throws Exception {

    @Cleanup Reader studieinfoXml = fsGetKurs(institution, language);
    return unmarshalStudieinfo(studieinfoXml);
  }

  /**
   * @param studieinfoXml
   *        the Reader is not closed in this method.
   */
  @SneakyThrows
  protected FsStudieinfo unmarshalStudieinfo(@NonNull Reader studieinfoXml) {

    net.sf.saxon.TransformerFactoryImpl trFactory = new net.sf.saxon.TransformerFactoryImpl();
    if (this.xmlSourceParser != null) {
      trFactory.setAttribute(FeatureKeys.SOURCE_PARSER_CLASS, this.xmlSourceParser);
    }
    @Cleanup InputStream transformerStream = transformerUrl.openStream();
    Source schemaSource = new StreamSource(transformerStream);
    Transformer stylesheet = trFactory.newTransformer(schemaSource);

    Source input = new StreamSource(studieinfoXml);

    @Cleanup StringWriter resultWriter = new StringWriter();
    Result result = new StreamResult(resultWriter);
    // This doens't work for CData content for some reason
    // JAXBResult result = new JAXBResult(jc);
    // return (FsStudieinfo)result.getResult();
    stylesheet.transform(input, result);

    @Cleanup Reader unmarshalSource = new StringReader(resultWriter.toString());
    //final Reader tmpSource = new InputStreamReader(new FileInputStream(resultFile), IOUtils.UTF8_CHARSET);

    JAXBContext jc = JAXBContext.newInstance(FsStudieinfo.class);
    FsStudieinfo sinfo = (FsStudieinfo)jc.createUnmarshaller().unmarshal(unmarshalSource);

    return sinfo;
  }

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

  protected Reader fsGetStudieprogram(String studieprogramkode, int year, String semester, boolean includeEP, String language) {
    Integer medUPinfo = includeEP ? INTEGER_1 : INTEGER_0;
    String studieinfoXml = fsServiceStudInfo.getStudieprogramSI(year, semester, medUPinfo, studieprogramkode, 
      INTEGER_MINUS_1, INTEGER_MINUS_1, INTEGER_MINUS_1, null, language);
    if (copyXML) {
      copyXML(COPY_PREFIX_PROGRAM, studieinfoXml);
    }
    return new StringReader(studieinfoXml);
  }
  
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

  protected Reader fsGetEmne(int institution, String emnekode, String versjonskode, int year, String semester, String language) {
    String studieinfoXml = fsServiceStudInfo.getEmneSI(institution, emnekode, versjonskode, INTEGER_MINUS_1, INTEGER_MINUS_1, 
      null, year, semester, language);
    
    if (copyXML) {
      copyXML(COPY_PREFIX_EMNE, studieinfoXml);
    }
    return new StringReader(studieinfoXml);
  }

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
