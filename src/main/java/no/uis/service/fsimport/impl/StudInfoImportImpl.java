package no.uis.service.fsimport.impl;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;

import no.uis.service.fsimport.StudInfoImport;
import no.uis.service.studinfo.data.Emne;
import no.uis.service.studinfo.data.FsStudieinfo;
import no.uis.service.studinfo.data.Kurs;
import no.uis.service.studinfo.data.Studieprogram;
import no.usit.fsws.wsdl.studinfo.StudInfoService;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class StudInfoImportImpl implements StudInfoImport {

  private StudInfoService fsServiceStudInfo;

  public void setFsServiceStudInfo(StudInfoService fsServiceStudInfo) {
    this.fsServiceStudInfo = fsServiceStudInfo;
  }

  public StudInfoService getFsServiceStudInfo() {
    return this.fsServiceStudInfo;
  }
  
  @Override
	public FsStudieinfo fetchStudyPrograms(int institution, int year, String semester, boolean includeEP,
			String language) throws Exception {

		Integer medUPinfo = includeEP ? INTEGER_1 : INTEGER_0;
		String studieinfoXml = fsServiceStudInfo.getStudieprogramSI(year,
				semester, medUPinfo, null, institution, INTEGER_MINUS_1, null, null, language);

    return unmarshalStudieinfo(studieinfoXml);
	}

  @Override
	public FsStudieinfo fetchSubjects(int institution, int year, String semester,
			String language) throws Exception {

	  String studieinfoXml = fsServiceStudInfo.getEmneSI(Integer.valueOf(institution), null, null,
				INTEGER_MINUS_1, null, null, year, semester, language);

    return unmarshalStudieinfo(studieinfoXml);
  }

  @Override
  public FsStudieinfo fetchCourses(int institution, int year, String semester, String language) throws Exception {

    String studieinfoXml = fsServiceStudInfo.getKursSI(Integer.valueOf(institution), INTEGER_MINUS_1, INTEGER_MINUS_1, INTEGER_MINUS_1, language);

    return unmarshalStudieinfo(studieinfoXml);
  }

  protected FsStudieinfo unmarshalStudieinfo(String studieinfoXml) throws JAXBException, SAXException {
    JAXBContext jc = JAXBContext.newInstance(FsStudieinfo.class.getPackage().getName());
    Unmarshaller um = jc.createUnmarshaller();
    
    XMLReader reader = XMLReaderFactory.createXMLReader();
    
    InputSource is = new InputSource(new StringReader(studieinfoXml));
    SAXSource source = new SAXSource(reader, is);
    
    return (FsStudieinfo)um.unmarshal(source);
  }
}
