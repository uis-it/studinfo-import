package no.uis.service.fsimport;


import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import no.uis.service.fsimport.impl.StudInfoImportImpl;
import no.uis.service.fsimport.impl.StudInfoImportImpl.StudinfoInterceptor;
import no.uis.service.model.ImportReport;
import no.uis.service.studinfo.data.KursType;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DateTimeSolrTest {

  private static final String VAAR = String.valueOf(new char[] {'V', '\u00c5', 'R'});
  private static StudInfoImportImpl studInfoImport;
  private static SolrServer solrServer;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
      new String[] {
        "fsimportPropsContext.xml" 
       ,"fsimportContext.xml"
       ,"studinfoMock.xml"
       ,"studinfoAnswerMockCurrentDate.xml"
       ,"cpmock.xml"
       ,"solrMock.xml"
       }, 
     true);

    studInfoImport = (StudInfoImportImpl) context.getBean("fsStudInfoImport", StudInfoImport.class);
    solrServer = (SolrServer)context.getBean("solrServer");
  }
  
  @Test
  public void testDateLowerLimit() throws Exception {

    StudinfoInterceptor interceptor = new StudinfoInterceptor() {
      @Override
      public void interceptStudinfo(List<?> items) throws Exception {
        for (Object item : items) {
          if (item instanceof KursType) {
            KursType kurs = (KursType)item;
            DatatypeFactory dtf = DatatypeFactory.newInstance();
            Calendar calToday = Calendar.getInstance();
            kurs.setDatoPubliseresFra(dtf.newXMLGregorianCalendar((GregorianCalendar)calToday));
            Calendar calTomorrow = (Calendar)calToday.clone();
            calTomorrow.add(Calendar.DAY_OF_MONTH, 1);
            kurs.setDatoPubliseresTil(dtf.newXMLGregorianCalendar((GregorianCalendar)calTomorrow));
          }
        }
      }};
      
    studInfoImport.setInterceptor(interceptor);
    ImportReport report = studInfoImport.importCourses(217, 2011, VAAR, "B", true);

    assertTrue(report.getEntry().isEmpty());
    
    SolrQuery params = new SolrQuery("publish_from_dt:[* TO NOW/DAY] AND publish_to_dt:[NOW/DAY TO *]");
    QueryResponse response = solrServer.query(params);
    assertFalse(response.getResults().isEmpty());
  }
  
  @Test
  public void testDateUpperLimit() throws Exception {
    StudinfoInterceptor interceptor = new StudinfoInterceptor() {
      @Override
      public void interceptStudinfo(List<?> items) throws Exception {
        for (Object item : items) {
          if (item instanceof KursType) {
            KursType kurs = (KursType)item;
            DatatypeFactory dtf = DatatypeFactory.newInstance();
            Calendar calToday = Calendar.getInstance();
            kurs.setDatoPubliseresTil(dtf.newXMLGregorianCalendar((GregorianCalendar)calToday));
            Calendar calYesterday = (Calendar)calToday.clone();
            calYesterday.add(Calendar.DAY_OF_MONTH, -1);
            kurs.setDatoPubliseresFra(dtf.newXMLGregorianCalendar((GregorianCalendar)calYesterday));
          }
        }
      }};
      
    studInfoImport.setInterceptor(interceptor);
    ImportReport report = studInfoImport.importCourses(217, 2011, VAAR, "B", true);

    assertTrue(report.getEntry().isEmpty());
    
    SolrQuery params = new SolrQuery("publish_from_dt:[* TO NOW/DAY] AND publish_to_dt:[NOW/DAY TO *]");
    QueryResponse response = solrServer.query(params);
    assertFalse(response.getResults().isEmpty());
  }
}
