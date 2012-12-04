package no.uis.service.fsimport.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import javax.xml.bind.JAXBContext;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import no.uis.service.fsimport.StudInfoImport;
import no.uis.service.studinfo.data.FsStudieinfo;
import no.usit.fsws.wsdl.studinfo.StudInfoService;

public class StudInfoImportImpl implements StudInfoImport {

  private StudInfoService fsServiceStudInfo;
  private URL transformerUrl;
  private static final Logger log = Logger.getLogger(StudInfoImportImpl.class);

  public void setFsServiceStudInfo(StudInfoService fsServiceStudInfo) {
    this.fsServiceStudInfo = fsServiceStudInfo;
  }

  public StudInfoService getFsServiceStudInfo() {
    return this.fsServiceStudInfo;
  }

  public void setTransformerUrl(URL transformerUrl) {
    this.transformerUrl = transformerUrl;
  }
  
  @Override
	public FsStudieinfo fetchStudyPrograms(int institution, int year, String semester, boolean includeEP,
			String language) throws Exception {

		Reader studieinfoXml = fsGetStudieprogram(institution, year, semester, includeEP, language);
		try {
		  return unmarshalStudieinfo(studieinfoXml);
		} finally {
		  studieinfoXml.close();
		}
	}

  @Override
	public FsStudieinfo fetchSubjects(int institution, int year, String semester,
			String language) throws Exception {

	  Reader studieinfoXml = fsGetEmne(institution, year, semester, language);
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

  protected Reader fsGetStudieprogram(int institution, int year, String semester, boolean includeEP, String language) {
    Integer medUPinfo = includeEP ? INTEGER_1 : INTEGER_0;
    String studieinfoXml = fsServiceStudInfo.getStudieprogramSI(year,
      semester, medUPinfo, null, institution, INTEGER_MINUS_1, null, null, language);
    return new StringReader(studieinfoXml);
  }
  
  protected Reader fsGetEmne(int institution, int year, String semester, String language) {
    String studieinfoXml = fsServiceStudInfo.getEmneSI(Integer.valueOf(institution), null, null,
      INTEGER_MINUS_1, null, null, year, semester, language);
    return new StringReader(studieinfoXml);
  }
  
  protected Reader fsGetKurs(int institution, String language) {
    String studieinfoXml = fsServiceStudInfo.getKursSI(Integer.valueOf(institution), INTEGER_MINUS_1, INTEGER_MINUS_1, INTEGER_MINUS_1, language);
    return new StringReader(studieinfoXml);
  }

  // see http://jarfiller.com/guide/jaxb/xslt.xhtml
  /**
   * 
   * @param studieinfoXml the Reader is not closed in this method.
   */
  protected FsStudieinfo unmarshalStudieinfo(Reader studieinfoXml) throws Exception {
    
    TransformerFactory trFactory = TransformerFactory.newInstance();
    Reader unmarshalSource = null;
    List<Runnable> cleanupTasks = new ArrayList<Runnable>(2); 
    
    try {
      if (transformerUrl != null) {
        Source schemaSource = new StreamSource(transformerUrl.openStream());
        Transformer stylesheet = trFactory.newTransformer(schemaSource);
    
        Source input = new StreamSource(studieinfoXml);
        
        final File resultFile = File.createTempFile("jaxb", ".xml");
        
        cleanupTasks.add(new Runnable() {
          @Override public void run() {
            resultFile.delete();
          }
        });
        Result result = new StreamResult(resultFile);
        
        // This doens't work for CData content for some reason
        //JAXBResult result = new JAXBResult(jc);
        //return (FsStudieinfo)result.getResult();
        
        stylesheet.transform(input, result);
        @SuppressWarnings("resource")
        final Reader _unmarshalSource = new InputStreamReader(new FileInputStream(resultFile), "UTF-8");
        cleanupTasks.add(new Runnable() {
          @Override public void run() {
            try {
              _unmarshalSource.close();
            } catch(IOException e) {
              log.warn("closing stream", e);
            }
          }
        });
        unmarshalSource = _unmarshalSource;
        
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
