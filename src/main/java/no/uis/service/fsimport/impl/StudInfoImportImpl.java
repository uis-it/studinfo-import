package no.uis.service.fsimport.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.sax.SAXSource;

import no.uis.service.fsimport.StudInfoImport;
import no.uis.service.fsimport.util.ImportReportUtil;
import no.uis.service.model.ImportReport;
import no.uis.service.studinfo.data.Emne;
import no.uis.service.studinfo.data.FsStudieinfo;
import no.uis.service.studinfo.data.Kurs;
import no.uis.service.studinfo.data.KurskategoriListeKurskategorikodeAndKurskategorinavnItem;
import no.uis.service.studinfo.data.Sted;
import no.uis.service.studinfo.data.Studieprogram;
import no.uis.service.studinfo.data.YESNOType;
import no.usit.fsws.wsdl.studinfo.StudInfoService;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
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
  
  private static final Integer INTEGER_0 = Integer.valueOf(0);
  private static final Integer INTEGER_1 = Integer.valueOf(1);
  private static final String ID_TOKEN_SEPARATOR = "_";
  
  /**
   * -1 means all possible values, e.g. all faculties
   */
	private static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
	
  private StudInfoService fsServiceStudInfo;

  private ThreadLocal<Map<String, CPArticleInfo>> courseDescriptionCache = new ThreadLocal<Map<String, CPArticleInfo>>() {
    @Override
    protected Map<String, CPArticleInfo> initialValue() {
      return Collections.emptyMap();
    }
  };
  
	private SolrServer solrServerCourse;
	private SolrServer solrServerProgram;
	private SolrServer solrServerSubject;
	
  private DomainUrl cpUrl;
  private XmlAccessorHolder xmlAccessorHolder;
  private int evuCategoryId = 5793;
  private StudinfoInterceptor interceptor;

	public void setFsServiceStudInfo(StudInfoService fsServiceStudInfo) {
		this.fsServiceStudInfo = fsServiceStudInfo;
	}

	public void setSolrServerCourse(SolrServer solrServer) {
    this.solrServerCourse = solrServer;
  }

	public void setSolrServerProgram(SolrServer solrServerProgram) {
    this.solrServerProgram = solrServerProgram;
  }

  public void setSolrServerSubject(SolrServer solrServerSubject) {
    this.solrServerSubject = solrServerSubject;
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

  public void setInterceptor(StudinfoInterceptor interceptor) {
    this.interceptor = interceptor;
  }
  
  @Override
	public ImportReport importStudyPrograms(int institution, int year, String semester, boolean includeEP,
			String language) throws Exception {

		Integer medUPinfo = includeEP ? INTEGER_1 : INTEGER_0;
		String studieinfoXml = fsServiceStudInfo.getStudieprogramSI(year,
				semester, medUPinfo, null, institution, INTEGER_MINUS_ONE, null, null, language);

		return importStudyInfoXml(SolrCategory.STUDIEPROGRAM, studieinfoXml);
	}

  @Override
	public ImportReport importSubjects(int institution, int year, String semester,
			String language) throws Exception {

	  String studieinfoXml = fsServiceStudInfo.getEmneSI(Integer.valueOf(institution), null, null,
				INTEGER_MINUS_ONE, null, null, year, semester, language);
		
	  return importStudyInfoXml(SolrCategory.EMNE, studieinfoXml);
  }

  @Override
  public ImportReport importCourses(int institution, int year, String semester, String language, boolean cleanBeforeUpdate) throws Exception {

    String studieinfoXml = fsServiceStudInfo.getKursSI(Integer.valueOf(institution), INTEGER_MINUS_ONE, INTEGER_MINUS_ONE, INTEGER_MINUS_ONE, language);

    Map<String, CPArticleInfo> descriptionCache = new HashMap<String, CPArticleInfo>();
    fillDescriptionCache(descriptionCache);
    courseDescriptionCache.set(descriptionCache);
    try {
      return importStudyInfoXml(SolrCategory.KURS, studieinfoXml);
    } finally {
      courseDescriptionCache.remove();
    }
  }

  protected ImportReport importStudyInfoXml(SolrCategory category, String studieinfoXml)
      throws JAXBException, SAXException, SolrServerException, IOException, Exception
      {
    ImportReport report = ImportReportUtil.newImportReport(category.toString());
    
    FsStudieinfo sinfo = unmarshalStudieinfo(studieinfoXml);
    
    
    interceptStudieInfo(sinfo);
    
    cleanCategory(category);
    
    pushItemsToSolr(sinfo, report);
    
    ImportReportUtil.stop(report);
    
    return report;
  }
  
  private void cleanCategory(SolrCategory category) throws SolrServerException, IOException {
    StringBuffer sb = new StringBuffer();
    for (String cat : category.getPartialCategories()) {
      if (sb.length() > 0) {
        sb.append(" AND ");
      }
      sb.append("cat:");
      sb.append(cat);
    }
    solrServerCourse.deleteByQuery(sb.toString());
  }

  private void interceptStudieInfo(FsStudieinfo sinfo) throws Exception {
    if (this.interceptor != null) {
      interceptor.interceptStudinfo(sinfo);
    }
  }

  /**
   * Courses (the FS part) are simply put into a Solr index.
   * The data from the FS database contains no namespace, whereas the one from the web service does.
   * 
   * @param sinfo the XML from either DB or WS
   * @param cleanBeforeUpdate purge courses before inserting new ones
   * @param report 
   */
  private void pushItemsToSolr(FsStudieinfo sinfo, ImportReport report) throws Exception {
    
    if (sinfo.isSetKurs()) {
      for (Kurs kurs : sinfo.getKurs()) {
        pushKursToSolr(kurs, report);
      }
    }
    if (sinfo.isSetEmne()) {
      for (Emne emne : sinfo.getEmne()) {
        pushEmneToSolr(emne, report);
      }
    }
    if (sinfo.isSetStudieprogram()) {
      for (Studieprogram program : sinfo.getStudieprogram()) {
        pushStudieprogramToSolr(program, report);
      }
    }
  }

  private void pushStudieprogramToSolr(Studieprogram prog, ImportReport report) {
    SolrInputDocument doc = new SolrInputDocument();
    
  }

  private void pushKursToSolr(Kurs kurs, ImportReport report) throws SolrServerException, IOException {
    SolrInputDocument doc = new SolrInputDocument();

    String courseId = kurs.getKursid().getKurskode() + ID_TOKEN_SEPARATOR + kurs.getKursid().getTidkode();
    doc.addField("id", "kurs" + ID_TOKEN_SEPARATOR + courseId);
    
    addCategories(doc, SolrCategory.KURS);

    doc.addField("name", kurs.getKursnavn());
    for (KurskategoriListeKurskategorikodeAndKurskategorinavnItem kursKat : kurs.getKurskategoriListe().getKurskategorikodeAndKurskategorinavnItems()) {
      doc.addField("course_category_code", kursKat.getItemKurskategorikode());
    }

    doc.addField("course_code_s", kurs.getKursid().getKurskode());
    doc.addField("course_time_code_s", kurs.getKursid().getTidkode());
    String date = dateToSolrString(kurs.getDatoFristSoknad());
    if (date != null) {
      doc.addField("application_deadline_dt", date);
    }
    date = dateToSolrString(kurs.getDatoPubliseresFra());
    if (date != null) {
      doc.addField("publish_from_dt", date);
    }
    date = dateToSolrString(kurs.getDatoPubliseresTil());
    if (date != null) {
      doc.addField("publish_to_dt", date);
    }
    doc.addField("course_contact_s", kurs.getEmail());
    
    CPArticleInfo cpinfo = courseDescriptionCache.get().get(courseId);
    if (cpinfo != null) {
      doc.addField("cp_article_id_l", cpinfo.getArticleId());
      doc.addField("cp_category_id_l", cpinfo.getCategoryId());
      doc.addField("text", cpinfo.getText());
    }

    solrServerCourse.add(doc, 3000);
  }

  private void pushEmneToSolr(Emne emne, ImportReport report) throws SolrServerException, IOException {
    SolrInputDocument doc = new SolrInputDocument();
    
    String emneId = emne.getEmneid().getInstitusjonsnr() + ID_TOKEN_SEPARATOR + emne.getEmneid().getEmnekode() + ID_TOKEN_SEPARATOR + emne.getEmneid().getVersjonskode();
    doc.addField("id", "emne" + ID_TOKEN_SEPARATOR + emneId);
    
    addCategories(doc, SolrCategory.EMNE);
    
    String adminAnsvarlig = null;
    String fagAnsvarlig = null;
    for (Sted sted : emne.getSted()) {
      if (sted.getType().equals("adminansvarlig")) {
        adminAnsvarlig = getStedCode(sted);
      } else if (sted.getType().equals("fagansvarlig")) {
        fagAnsvarlig = getStedCode(sted);
      }
    }
    if (adminAnsvarlig != null) {
      doc.addField("adminansvarlig_s", adminAnsvarlig);
    }
    if (fagAnsvarlig != null) {
      doc.addField("fagansvarlig_s", fagAnsvarlig);
    }
    doc.addField("emnenavn_s", emne.getEmnenavn());
    
    doc.addField("antall-undsemester_i", emne.getAntallUndsemester());
    doc.addField("eksamensemester_s", emne.getEksamenssemester());
    doc.addField("inngar-i-studieprogram_ms", emne.getInngarIStudieprogram());
    doc.addField("inngar-i-fag_ms", emne.getInngarIFag());
    doc.addField("nuskode_s", emne.getNuskode());
    doc.addField("periode-eks-start_s", emne.getPeriodeEks().getForstegang());
    doc.addField("periode-eks-end_s", emne.getPeriodeEks().getSistegang());
    doc.addField("periode-und-start_s", emne.getPeriodeUnd().getForstegang());
    doc.addField("periode-und-end_s", emne.getPeriodeUnd().getSistegang());
    doc.addField("sprak_s", emne.getSprak());
    doc.addField("status-oblig_b", isTrue(emne.getStatusOblig()));
    doc.addField("status-privatist_b", isTrue(emne.getStatusPrivatist()));
    doc.addField("studieniva_s", emne.getStudieniva());
    doc.addField("studiepoeng", emne.getStudiepoeng());
    doc.addField("undervisningssemester_s", emne.getUndervisningssemester());
    doc.addField("emnetype_s", emne.getEmnetype());
    //doc.addField(", value)
    
    solrServerSubject.add(doc, 3000);
  }

  private Boolean isTrue(YESNOType yn) {
    if (yn != null) {
      if (yn.equals(YESNOType.J)) {
        return Boolean.TRUE;
      }
    }
    
    return Boolean.FALSE;
  }
  
  private String getStedCode(Sted sted) {
    StringBuilder sb = new StringBuilder();
    
    sb.append(sted.getInstitusjonsnr().intValue());
    sb.append(ID_TOKEN_SEPARATOR);
    sb.append(sted.getFakultetsnr().intValue());
    sb.append(ID_TOKEN_SEPARATOR);
    sb.append(sted.getInstituttnr().intValue());
    sb.append(ID_TOKEN_SEPARATOR);
    sb.append(sted.getGruppenr().intValue());
    return sb.toString();
  }

  private static void addCategories(SolrInputDocument doc, SolrCategory category) {
    for (String pCat : category.getPartialCategories()) {
      doc.addField("cat", pCat);
    }
  }
  
  private static String dateToSolrString(XMLGregorianCalendar xcal) {
    if (xcal != null) {
      return String.format("%d-%02d-%02dT%02d:%02d:%02dZ", xcal.getYear(), xcal.getMonth(), xcal.getDay(), xcal.getHour(), xcal.getMinute(), xcal.getMinute());
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
    
   void interceptStudinfo(FsStudieinfo sinfo) throws Exception;  
  }
  
}
