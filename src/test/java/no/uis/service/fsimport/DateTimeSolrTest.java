package no.uis.service.fsimport;


import java.util.Date;
import java.util.List;
import no.uis.service.fsimport.impl.StudInfoImportImpl;
import no.uis.service.fsimport.impl.StudInfoImportImpl.StudinfoInterceptor;
import no.uis.service.model.ImportReport;
import no.uis.service.studinfo.data.FsStudieinfoKursOrEmneOrStudieprogramItem;
import no.uis.service.studinfo.data.KursType;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DateTimeSolrTest {

  private static StudInfoImportImpl studInfoImport;
  private static SolrServer solrServer;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
      new String[] {
        "fsimportPropsContext.xml" 
       ,"fsJdbcClientContext.xml" 
       //,"personormContext.xml"
       ,"studinfoOrmContext.xml"
       ,"fsimportContext.xml"
       ,"studinfoMock.xml"
       ,"studinfoAnswerMockCurrentDate.xml"
       ,"cpmock.xml"
       }, 
     true);

    studInfoImport = (StudInfoImportImpl) context.getBean("fsStudInfoImport", StudInfoImport.class);
    solrServer = (SolrServer)context.getBean("solrServer");
  }
  
  @Test
  public void testDateLowerLimit() throws Exception {

    StudinfoInterceptor interceptor = new StudinfoInterceptor() {
      @Override
      public void interceptStudinfo(List<FsStudieinfoKursOrEmneOrStudieprogramItem> items) {
        for (FsStudieinfoKursOrEmneOrStudieprogramItem item : items) {
          KursType kurs = item.getItemKurs();
          if (kurs != null) {
            long now = System.currentTimeMillis();
            kurs.setDatoPubliseresFraItem(new Date(now));
            kurs.setDatoPubliseresTilItem(new Date(now + 86400000L)); // tomorrow
          }
        }
      }};
      
    studInfoImport.setInterceptor(interceptor);
    ImportReport report = studInfoImport.importCourses(217, 2011, "VÅR", "B", true);

    assertTrue(report.getEntry().isEmpty());
    
    SolrQuery params = new SolrQuery("publish_from_dt:[* TO NOW/DAY] AND publish_to_dt:[NOW/DAY TO *]");
    QueryResponse response = solrServer.query(params);
    assertFalse(response.getResults().isEmpty());
  }
  
  @Test
  public void testDateUpperLimit() throws Exception {
    StudinfoInterceptor interceptor = new StudinfoInterceptor() {
      @Override
      public void interceptStudinfo(List<FsStudieinfoKursOrEmneOrStudieprogramItem> items) {
        for (FsStudieinfoKursOrEmneOrStudieprogramItem item : items) {
          KursType kurs = item.getItemKurs();
          if (kurs != null) {
            long now = System.currentTimeMillis();
            kurs.setDatoPubliseresFraItem(new Date(now - 86400000L)); // yesterday
            kurs.setDatoPubliseresTilItem(new Date(now)); 
          }
        }
      }};
      
    studInfoImport.setInterceptor(interceptor);
    ImportReport report = studInfoImport.importCourses(217, 2011, "VÅR", "B", true);

    assertTrue(report.getEntry().isEmpty());
    
    SolrQuery params = new SolrQuery("publish_from_dt:[* TO NOW/DAY] AND publish_to_dt:[NOW/DAY TO *]");
    QueryResponse response = solrServer.query(params);
    assertFalse(response.getResults().isEmpty());
  }
}
