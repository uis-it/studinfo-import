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

  enum SolrCategory {
    KURS,
    EMNE,
    STUDIEPROGRAM;
    
    private static final String SOLR_CATEGORY_STUDINFO = "studinfo";

    private final String code;
    private final String[] partialCategories;
    
    private SolrCategory() {
      String lcName = this.name().toLowerCase();
      code = SOLR_CATEGORY_STUDINFO + "_" + lcName;
      partialCategories = new String[] {SOLR_CATEGORY_STUDINFO, lcName};
    }
    
    @Override
    public String toString() {
      return code;
    }
    
    public String[] getPartialCategories() {
      return partialCategories;
    }
  }
  
  private static final Integer INTEGER_0 = Integer.valueOf(0);
  private static final Integer INTEGER_1 = Integer.valueOf(1);
  
  /**
   * -1 means all possible values, e.g. all faculties
   */
	private static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
	
  private StudInfoService fsServiceStudInfo;
  private StudinfoInterceptor interceptor;

  public void setFsServiceStudInfo(StudInfoService fsServiceStudInfo) {
    this.fsServiceStudInfo = fsServiceStudInfo;
  }

  public StudInfoService getFsServiceStudInfo() {
    return this.fsServiceStudInfo;
  }
  
  public void setInterceptor(StudinfoInterceptor interceptor) {
    this.interceptor = interceptor;
  }
  
  @Override
	public List<Studieprogram> fetchStudyPrograms(int institution, int year, String semester, boolean includeEP,
			String language) throws Exception {

		Integer medUPinfo = includeEP ? INTEGER_1 : INTEGER_0;
		String studieinfoXml = fsServiceStudInfo.getStudieprogramSI(year,
				semester, medUPinfo, null, institution, INTEGER_MINUS_ONE, null, null, language);

    FsStudieinfo sinfo = unmarshalStudieinfo(studieinfoXml);
    sinfo = interceptStudieInfo(sinfo);
		return sinfo.getStudieprogram();
	}

  @Override
	public List<Emne> fetchSubjects(int institution, int year, String semester,
			String language) throws Exception {

	  String studieinfoXml = fsServiceStudInfo.getEmneSI(Integer.valueOf(institution), null, null,
				INTEGER_MINUS_ONE, null, null, year, semester, language);

    FsStudieinfo sinfo = unmarshalStudieinfo(studieinfoXml);
    sinfo = interceptStudieInfo(sinfo);
    return sinfo.getEmne();
  }

  @Override
  public List<Kurs> fetchCourses(int institution, int year, String semester, String language) throws Exception {

    String studieinfoXml = fsServiceStudInfo.getKursSI(Integer.valueOf(institution), INTEGER_MINUS_ONE, INTEGER_MINUS_ONE, INTEGER_MINUS_ONE, language);

    FsStudieinfo sinfo = unmarshalStudieinfo(studieinfoXml);
    sinfo = interceptStudieInfo(sinfo);
    return sinfo.getKurs();
  }

  protected FsStudieinfo unmarshalStudieinfo(String studieinfoXml) throws JAXBException, SAXException {
    JAXBContext jc = JAXBContext.newInstance(FsStudieinfo.class.getPackage().getName());
    Unmarshaller um = jc.createUnmarshaller();
    
    XMLReader reader = XMLReaderFactory.createXMLReader();
    
    InputSource is = new InputSource(new StringReader(studieinfoXml));
    SAXSource source = new SAXSource(reader, is);
    
    return (FsStudieinfo)um.unmarshal(source);
  }

  public interface StudinfoInterceptor {
    
    FsStudieinfo interceptStudinfo(FsStudieinfo sinfo) throws Exception;  
   }
   
   
  private FsStudieinfo interceptStudieInfo(FsStudieinfo sinfo) throws Exception {
    if (this.interceptor != null) {
      return interceptor.interceptStudinfo(sinfo);
    }
    return sinfo;
  }

  
}
