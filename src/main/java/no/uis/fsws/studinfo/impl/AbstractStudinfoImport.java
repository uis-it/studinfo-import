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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import net.sf.saxon.lib.FeatureKeys;
import no.uis.fsws.studinfo.StudInfoImport;
import no.uis.fsws.studinfo.data.FsStudieinfo;

import org.apache.cxf.helpers.IOUtils;

/**
 * Abstract class makes it easier to write test code.
 */
@Log4j
public abstract class AbstractStudinfoImport implements StudInfoImport {

  @Setter private URL transformerUrl;

  @Setter private String xmlSourceParser;

  protected abstract Reader fsGetKurs(int institution, String language);

  protected abstract Reader fsGetEmne(int institution, int faculty, int year, String semester, String language);

  protected abstract Reader fsGetEmne(int institution, String emnekode, String versjonskode, int year, String semester, String language);
  
  protected abstract Reader fsGetStudieprogram(int institution, int faculty, int year, String semester, boolean includeEP,
      String language);

  protected abstract Reader fsGetStudieprogram(String studieprogramkode, int year, String semester, boolean includeEP, String language);

  @Override
  public FsStudieinfo fetchStudyPrograms(int institution, int faculty, int year, String semester, boolean includeEP,
      String language) throws Exception
  {

    Reader studieinfoXml = fsGetStudieprogram(institution, faculty, year, semester, includeEP, language);
    try {
      return unmarshalStudieinfo(studieinfoXml);
    } finally {
      if (studieinfoXml != null) {
        studieinfoXml.close();
      }
    }
  }

  @Override
  public FsStudieinfo fetchStudyProgram(String studieprogramkode, int year, String semester, boolean includeEP,
      String language) throws Exception
  {

    Reader studieinfoXml = fsGetStudieprogram(studieprogramkode, year, semester, includeEP, language);
    try {
      return unmarshalStudieinfo(studieinfoXml);
    } finally {
      if (studieinfoXml != null) {
        studieinfoXml.close();
      }
    }
  }

  @Override
  public FsStudieinfo fetchSubjects(int institution, int faculty, int year, String semester, String language) throws Exception {

    Reader studieinfoXml = fsGetEmne(institution, faculty, year, semester, language);
    try {
      return unmarshalStudieinfo(studieinfoXml);
    } finally {
      studieinfoXml.close();
    }
  }

  @Override
  public FsStudieinfo fetchCourses(int institution, int year, String semester, String language) throws Exception {

    Reader studieinfoXml = fsGetKurs(institution, language);
    try {
      return unmarshalStudieinfo(studieinfoXml);
    } finally {
      studieinfoXml.close();
    }
  }

  /**
   * @param studieinfoXml
   *        the Reader is not closed in this method.
   */
  protected FsStudieinfo unmarshalStudieinfo(Reader studieinfoXml) throws Exception {
    if (studieinfoXml == null) {
      return null;
    }
    Reader unmarshalSource = null;
    List<Runnable> cleanupTasks = new ArrayList<Runnable>(2);

    try {
      if (transformerUrl != null) {
        net.sf.saxon.TransformerFactoryImpl trFactory = new net.sf.saxon.TransformerFactoryImpl();
        if (this.xmlSourceParser != null) {
          trFactory.setAttribute(FeatureKeys.SOURCE_PARSER_CLASS, this.xmlSourceParser);
        }
        Source schemaSource = new StreamSource(transformerUrl.openStream());
        Transformer stylesheet = trFactory.newTransformer(schemaSource);

        Source input = new StreamSource(studieinfoXml);

        final File resultFile = File.createTempFile("jaxb", ".xml");

        cleanupTasks.add(new Runnable() {
          @Override
          public void run() {
            resultFile.delete();
          }
        });
        Result result = new StreamResult(resultFile);

        // This doens't work for CData content for some reason
        // JAXBResult result = new JAXBResult(jc);
        // return (FsStudieinfo)result.getResult();

        stylesheet.transform(input, result);
        @SuppressWarnings("resource")
        final Reader tmpSource = new InputStreamReader(new FileInputStream(resultFile), IOUtils.UTF8_CHARSET);
        cleanupTasks.add(new Runnable() {
          @Override
          public void run() {
            try {
              tmpSource.close();
            } catch(IOException e) {
              log.warn("closing stream", e);
            }
          }
        });
        unmarshalSource = tmpSource;

      } else {
        unmarshalSource = studieinfoXml;
      }

      JAXBContext jc = JAXBContext.newInstance(FsStudieinfo.class);
      FsStudieinfo sinfo = (FsStudieinfo)jc.createUnmarshaller().unmarshal(unmarshalSource);

      return sinfo;
    } finally {
      for (Runnable task : cleanupTasks) {
        task.run();
      }
    }
  }
}