package no.uis.service.fsimport.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;

import no.uis.service.fsimport.StudInfoImport;
import no.uis.service.fsimport.util.ImportReportUtil;
import no.uis.service.model.ImportReport;
import no.uis.service.model.studinfo.StudInfo;
import no.uis.service.model.studinfo.courses.CourseCategory;
import no.uis.service.model.studinfo.courses.FSCourse;
import no.uis.service.model.studinfo.courses.FSCourseId;
import no.uis.service.studinfo.data.FsStudieinfo;
import no.uis.service.studinfo.data.FsStudieinfoKursOrEmneOrStudieprogramItem;
import no.uis.service.studinfo.data.KursType;
import no.uis.service.studinfo.data.Kursid;
import no.uis.service.studinfo.data.KurskategoriType;
import no.uis.service.studinfo.data.YESNOType;
import no.uis.service.studinfo.persistence.StudInfoDAO;
import no.usit.fsws.wsdl.studinfo.StudInfoService;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest.ACTION;
import org.apache.solr.common.SolrInputDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.corepublish.api.Accessor;
import com.corepublish.api.Article;
import com.corepublish.api.ArticleQuery;
import com.corepublish.api.ArticleQueryResult;
import com.corepublish.api.article.element.ArticleElement;
import com.corepublish.api.article.element.ArticleElementType;
import com.corepublish.api.article.element.NewTextElement;
import com.corepublish.api.article.element.ProgramaticElement;
import com.corepublish.api.article.element.TextElement;
import com.corepublish.api.article.richtext.HtmlFragmentToken;
import com.corepublish.api.article.richtext.Token;
import com.corepublish.impl.defaultt.DefaultArticleQuery;
import com.corepublish.impl.xml.XmlAccessorHolder;
import com.corepublish.util.DomainUrl;

public class StudInfoImportImpl implements StudInfoImport {

  private static final String FILE_CHARSET = "ISO-8859-1";
  private static Logger log = Logger.getLogger(StudInfoImportImpl.class);
  
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
  
  private static final String XML_PREFIX = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>";
  private static final Integer INTEGER_0 = Integer.valueOf(0);
  private static final Integer INTEGER_1 = Integer.valueOf(1);
  private static final String ID_TOKEN_SEPARATOR = "_";
  
  /**
   * -1 means all possible values, e.g. all faculties
   */
	private static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
	
  private StudInfoService fsServiceStudInfo;
  
	private StudInfoDAO studInfoDao;
	
	private SolrServer solrServer;
  private DomainUrl cpUrl;
  private XmlAccessorHolder xmlAccessorHolder;
  private int evuCategoryId = 5793;
  private boolean doSaveCourseXml;
  private StudinfoInterceptor interceptor;

	public void setFsServiceStudInfo(StudInfoService fsServiceStudInfo) {
		this.fsServiceStudInfo = fsServiceStudInfo;
	}

	public void setStudInfoDao(StudInfoDAO studInfoDao) {
		this.studInfoDao = studInfoDao;
	}

	public void setSolrServer(SolrServer solrServer) {
    this.solrServer = solrServer;
  }

	public void setCpUrl(DomainUrl cpUrl) {
	  this.cpUrl = cpUrl; 
	}
	
	public void setCpAccessorHolder(XmlAccessorHolder holder) {
	  this.xmlAccessorHolder = holder;
	}
	
  public void setEvuCategoryId(int evuCategoryId) {
    this.evuCategoryId = evuCategoryId;
  }

  public void setDoSaveCourseXml(boolean doSaveCourseXml) {
    this.doSaveCourseXml = doSaveCourseXml;
  }

  public void setInterceptor(StudinfoInterceptor interceptor) {
    this.interceptor = interceptor;
  }
  
  @Override
	public ImportReport importStudyPrograms(int institution, int year, String semester, boolean includeEP,
			String language) throws Exception {

	  ImportReport report = ImportReportUtil.newImportReport("studprog");

		Integer medUPinfo = includeEP ? INTEGER_1 : INTEGER_0;
		String studieprogramSI = fsServiceStudInfo.getStudieprogramSI(year,
				semester, medUPinfo, null, institution, INTEGER_MINUS_ONE, null, null, language);

		savePackage(PACKAGE_STUDY_PROGRAM, year, semester, language, studieprogramSI);
		
		ImportReportUtil.stop(report);
		return report;
	}

	@Override
	public ImportReport importSubjects(int institution, int year, String semester,
			String language) throws Exception {

	  ImportReport report = ImportReportUtil.newImportReport("emne");

	  String studieinfoXml = fsServiceStudInfo.getEmneSI(Integer.valueOf(institution), null, null,
				INTEGER_MINUS_ONE, null, null, year, semester, language);
		
	  
    if (this.doSaveCourseXml) {
      writeToFile(studieinfoXml);
    }

    FsStudieinfo sinfo = unmarshalStudieinfo(studieinfoXml);
    
		savePackage(PACKAGE_SUBJECT, year, semester, language, studieinfoXml);

		List<FsStudieinfoKursOrEmneOrStudieprogramItem> studieinfos = sinfo.getKursOrEmneOrStudieprogramItems();
		
		saveSubjects(studieinfos, true, report);
		
		ImportReportUtil.stop(report);
		return report;
	}

  @Override
  public ImportReport importCourses(int institution, int year, String semester, String language, boolean cleanBeforeUpdate) throws Exception {

    ImportReport report = ImportReportUtil.newImportReport("kurs");
    
    String studieinfoXml = fsServiceStudInfo.getKursSI(Integer.valueOf(institution), INTEGER_MINUS_ONE, INTEGER_MINUS_ONE, INTEGER_MINUS_ONE, language);

    if (this.doSaveCourseXml) {
      writeToFile(studieinfoXml);
    }
    
    FsStudieinfo sinfo = unmarshalStudieinfo(studieinfoXml);
    
    List<FsStudieinfoKursOrEmneOrStudieprogramItem> studieinfos = sinfo.getKursOrEmneOrStudieprogramItems();
    
    interceptStudieInfo(studieinfos);
    
    saveCourses(studieinfos, cleanBeforeUpdate, report);
    
    ImportReportUtil.stop(report);
    
    return report;
  }

  private void interceptStudieInfo(List<FsStudieinfoKursOrEmneOrStudieprogramItem> studieinfos) {
    if (this.interceptor != null) {
      interceptor.interceptStudinfo(studieinfos);
    }
  }

  /**
   * Courses (the FS part) are simply put into a Solr index.
   * The data from the FS database contains no namespace, whereas the one from the web service does.
   * 
   * @param studieinfos the XML from either DB or WS
   * @param cleanBeforeUpdate purge courses before inserting new ones
   * @param report 
   */
  private void saveCourses(List<FsStudieinfoKursOrEmneOrStudieprogramItem> studieinfos, boolean cleanBeforeUpdate, ImportReport report) throws Exception {
    
    List<FSCourse> courseList = mapKursTypeFSCourseList(studieinfos, report);
    
    deleteSolrIndex(SolrCategory.KURS);
    updateCoursesSolrIndex(SolrCategory.KURS, courseList);
    solrServer.commit();
  }

  private void saveSubjects(List<FsStudieinfoKursOrEmneOrStudieprogramItem> studieinfos, boolean cleanBeforeUpdate, ImportReport report) {
    //List<FSSubject> subjects = mapEmnerToSubjects(studieinfos, report);
    
  }
  
  private void writeToFile(String courses) throws FileNotFoundException, IOException {
    File xmlFile = new File(System.getProperty("java.io.tmpdir"), "evu-kurs.xml");
    OutputStream output = new FileOutputStream(xmlFile, false);
    IOUtils.write(String.format("<?xml version=\"1.0\" encoding=\"%s\"?>\n", FILE_CHARSET), output);
    IOUtils.write(courses, output, FILE_CHARSET);
    output.close();
  }
  
  private void updateCoursesSolrIndex(SolrCategory solrCategory, List<FSCourse> courseList) throws SolrServerException, IOException {
    if (solrServer == null) {
      return;
    }

    List<SolrInputDocument> docList = new ArrayList<SolrInputDocument>(courseList.size());
    Map<String, CPArticleInfo> descriptionCache = new HashMap<String, CPArticleInfo>();
    fillDescriptionCache(descriptionCache);
    for (FSCourse fsCourse : courseList) {
      SolrInputDocument doc = new SolrInputDocument();

      doc.addField("category", solrCategory.toString()); // single valued
      
      String courseId = fsCourse.getCourseId().toId(ID_TOKEN_SEPARATOR);
      doc.addField("id", solrCategory + ID_TOKEN_SEPARATOR + courseId);
      
      for (String cat : solrCategory.getPartialCategories()) {
        doc.addField("cat", cat);
      }
      
      doc.addField("name", fsCourse.getName());
      for (CourseCategory category : fsCourse.getCourseCategoryList()) {
        doc.addField("course_category_code", category.getCourseCategoryCode());
      }
      doc.addField("course_code_s", fsCourse.getCourseId().getCourseCode());
      doc.addField("course_time_code_s", fsCourse.getCourseId().getTimeCode());
      String date = dateToSolrString(fsCourse.getApplicationDeadline());
      if (date != null) {
        doc.addField("application_deadline_dt", date);
      }
      date = dateToSolrString(fsCourse.getPublishDateFrom());
      if (date != null) {
        doc.addField("publish_from_dt", date);
      }
      date = dateToSolrString(fsCourse.getPublishDateTo());
      if (date != null) {
        doc.addField("publish_to_dt", date);
      }
      doc.addField("course_contact_s", fsCourse.getEmail());
      
      CPArticleInfo cpinfo = descriptionCache.get(courseId);
      if (cpinfo != null) {
        doc.addField("cp_article_id_l", cpinfo.getArticleId());
        doc.addField("cp_category_id_l", cpinfo.getCategoryId());
        doc.addField("text", cpinfo.getText());
      }

      docList.add(doc);
    }

    UpdateRequest update = new UpdateRequest();
    update.setAction(ACTION.COMMIT, false, false);
    update.add(docList);
    update.process(solrServer);
  }

  private static String dateToSolrString(Date date) {
    if (date != null) {
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      String s = df.format(date);
      return s + "Z";
    }
    return null;
  }

  private void fillDescriptionCache(Map<String, CPArticleInfo> descriptionCache) {
    Accessor cpAccessor = null;
    try {
      cpAccessor = xmlAccessorHolder.getAccessor(cpUrl);
    } catch(Exception ex) {
      log.warn("CorePublish access disabled", ex);
      return;
    }
    if (cpAccessor == null) {
      log.warn("CorePublish access disabled");
      return;
    }
    ArticleQuery articleQuery = new DefaultArticleQuery();
    articleQuery.includeCategoryId(evuCategoryId, true);
    articleQuery.includeTemplateIds(10, 11); // EVU article and EVU category article template
    List<ArticleQueryResult> aqr = cpAccessor.getArticleQueryResult(articleQuery);
    
    List<Integer> articleIds = cpAccessor.getArticleIds(aqr);
  
    for (Integer articleId : articleIds) {
      
      try {
        Article article = cpAccessor.getArticle(articleId.intValue());
        List<ArticleElement> elements = article.getArticleElements().getElements();
        String kursKode = null;
        String tidKode = null;
        String mainText = "";
        for (ArticleElement articleElement : elements) {
          String pName = articleElement.getProgrammaticName();
          String value = getStringValue(articleElement, cpAccessor);
          if (pName.equalsIgnoreCase("kurskode")) {
            kursKode = value;
          } else if (pName.equalsIgnoreCase("tidskode")) {
            tidKode = value;
          } else if (pName.equalsIgnoreCase("maintext")) {
            mainText = value;
          }
        }
        if (kursKode != null && tidKode != null) {
          CPArticleInfo cpinfo = new CPArticleInfo(kursKode + ID_TOKEN_SEPARATOR + tidKode);
          cpinfo.setText(mainText);
          cpinfo.setArticleId(article.getId());
          cpinfo.setCategoryId(article.getMainCategoryId());
          descriptionCache.put(cpinfo.getId(), cpinfo);
        }
      } catch(Exception ex) {
        log.warn("Problem with article " + articleId, ex);
      }
    }
  }

  private String getStringValue(ArticleElement ae, Accessor cpAccessor) {
    ArticleElementType aeType = ae.getArticleElementType();
    
    String value;
    switch(aeType) {
      case PROGRAMMATIC:
        ProgramaticElement progElem = (ProgramaticElement)ae;
        value = cpAccessor.getProgrammaticElementObjectValue(progElem);
        break;
        
      case TEXT:
        TextElement txt = (TextElement)ae;
        value = txt.getText();
        break;
        
      case NEWTEXT:
        NewTextElement ntxt = (NewTextElement)ae;
        List<Token> tokens = ntxt.getContent().getTokens();
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
          if (token instanceof HtmlFragmentToken) {
            HtmlFragmentToken html = (HtmlFragmentToken)token;
            sb.append(html.getHtml());
            sb.append(' ');
          }
        }
        value = sb.toString();
        break;
        
      default:
        value = "";
        break;
    }
    return value;
  }

  private void deleteSolrIndex(SolrCategory solrCategory) throws SolrServerException, IOException {
    if (solrServer == null) {
      return;
    }
    solrServer.deleteByQuery("category:"+solrCategory.toString());
  }

  private List<FSCourse> mapKursTypeFSCourseList(List<FsStudieinfoKursOrEmneOrStudieprogramItem> studieinfos, ImportReport report) {
    
    List<FSCourse> l = new ArrayList<FSCourse>(studieinfos.size());
    for (FsStudieinfoKursOrEmneOrStudieprogramItem kursType : studieinfos) {
      try {
        FSCourse course = mapKursTypeToFSCourse(kursType);
        if (course != null) {
          l.add(course);
        }
      } catch(Exception e) {
        ImportReportUtil.add(report, e);
      }
    }
    return l;
  }

  /**
   * Map FS data type {@link KursType} to our data type {@link FSCourse}. 
   * TODO move to mapping component ?
   * @param kursType
   * @return
   */
  private FSCourse mapKursTypeToFSCourse(FsStudieinfoKursOrEmneOrStudieprogramItem kursObject) {
    
    KursType kursType = kursObject.getItemKurs();
    if (kursType == null) {
      return null;
    }

    FSCourse c = new FSCourse();

    c.setLanguage(kursType.getSprak());
    
    Kursid kursid = kursType.getKursid();
    if (kursid == null) {
      throw new IllegalArgumentException("kursid is null");
    }
    c.setCourseId(new FSCourseId(kursid.getKurskode(), kursid.getTidkode()));
    
    c.setName(kursType.getKursnavn());
    c.setAdminSupervisor(INTEGER_1);
    c.setLecturer(INTEGER_1);
    c.setApplicationDeadline(kursType.getDatoFristSoknadItem());
    c.setAdmissionDeadline(kursType.getDatoOpptakTilItem());
    c.setAdmissionFrom(kursType.getDatoOpptakTilItem());
    c.setEmail(kursType.getEmail());
    if (kursType.isSetFjernundervisning()) {
      c.setRemoteTuition(kursType.getFjernundervisning().equals(YESNOType.Y));
    }
    if (kursType.isSetDesentralUndervisning()) {
      c.setDecentralizedTuition(kursType.getDesentralUndervisning().equals(YESNOType.Y));
    }
    if (kursType.isSetNettbasertUndervisning()) {
      c.setNetbasedTuition(kursType.getNettbasertUndervisning().equals(YESNOType.Y));
    }
    if (kursType.isSetKanTilbys()) {
      c.setOffered(kursType.getKanTilbys().equals(YESNOType.Y));
    }
    if (kursType.isSetSkalAvholdes()) {
      c.setActive(kursType.getSkalAvholdes().equals(YESNOType.Y));
    }
    c.setPublishDateFrom(kursType.getDatoPubliseresFraItem());
    c.setPublishDateTo(kursType.getDatoPubliseresTilItem());

    List<CourseCategory> categoryList;
    List<KurskategoriType> kursItems = kursType.getKurskategoriListe();
    if (kursItems.isEmpty()) {
      categoryList = Collections.emptyList();
    } else {
      categoryList = new ArrayList<CourseCategory>();
      for (KurskategoriType item : kursItems) {
        categoryList.add(new CourseCategory(item.getKurskategorikode(), item.getKurskategorinavn()));
      }
    }
    c.setCourseCategoryList(categoryList);
    return c;
  }

//  private List<FSSubject> mapEmnerToSubjects(List<FsStudieinfoKursOrEmneOrStudieprogramItem> studieinfos, ImportReport report) {
//    List<FSSubject> subjects = new ArrayList<FSSubject>(studieinfos.size());
//    
//    for (FsStudieinfoKursOrEmneOrStudieprogramItem fsItems : studieinfos) {
//      EmneType emne = fsItems.getItemEmne();
//      if (emne != null) {
////        FSSubject subject = new FSSubject();
////        subjects.add(subject);
//      }
//    }
//    
//    return subjects;
//  }

  /**
   * Namespace handling taken from http://stackoverflow.com/questions/277502/jaxb-how-to-ignore-namespace-during-unmarshalling-xml-document  
   */
  private FsStudieinfo unmarshalStudieinfo(String studieinfoXml) throws JAXBException, SAXException {
    JAXBContext jc = JAXBContext.newInstance(FsStudieinfo.class.getPackage().getName());
    Unmarshaller um = jc.createUnmarshaller();
    
    XMLReader reader = XMLReaderFactory.createXMLReader();
    
    StudinfoNamespaceFilter readerFilter = new StudinfoNamespaceFilter("http://fsws.usit.no/schemas/studinfo", true);
    readerFilter.setParent(reader);
    
    InputSource is = new InputSource(new StringReader(studieinfoXml));
    SAXSource source = new SAXSource(readerFilter, is);
    
    return (FsStudieinfo)um.unmarshal(source);
  }

  /**
   * Save the data to the tblOracleXml table where it can be picked up by DTS packages. 
   * The data fetched from the database contains an <?xml ?> header but no namespace.
   * The data fetched from the web service contains no <?xml ?> header but a namespace.
   */
  private void savePackage(String studinfoPackage, int year, String semester, String language, String data) {
    
    StudInfo studInfo = new StudInfo();
    if (data.indexOf(XML_PREFIX) < 0) {
      data = XML_PREFIX + data;
    }
    studInfo.setXmlString(data);
    studInfo.setDate(Calendar.getInstance());
    studInfo.setSprak(language);
    studInfo.setSemester(semester);
    studInfo.setYear(year);
    studInfo.setPackage(studinfoPackage);
    
    studInfoDao.saveXML(studInfo);
  }
  
  private static class CPArticleInfo {
    private final String id;
    private String text;
    private int articleId;
    private int categoryId; 
    
    public CPArticleInfo(String cacheId) {
      this.id = cacheId;
    }

    public void setText(String mainText) {
      this.text = mainText;
    }

    public void setArticleId(int id) {
      this.articleId = id;
    }

    public void setCategoryId(int evuCategoryId) {
      this.categoryId = evuCategoryId;
    }

    public String getId() {
      return id;
    }

    public String getText() {
      return text;
    }

    public int getArticleId() {
      return articleId;
    }

    public int getCategoryId() {
      return categoryId;
    }
  }

  public interface StudinfoInterceptor {
    
   void interceptStudinfo(List<FsStudieinfoKursOrEmneOrStudieprogramItem> items);  
  }
  
}
