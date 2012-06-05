package no.uis.service.fsimport;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import no.uis.service.fsimport.SolrTestRunner.ServerConfig;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

@RunWith(SolrTestRunner.class)
@ServerConfig(home="src/test/resources/solr1/")
public class StudySubjectSolrTest {

  @BeforeClass
  public static void init() throws Exception {
    StaticApplicationContext appContext = new StaticApplicationContext();
    SolrServer solrServer = new HttpSolrServer("http://Localhost:8080/solr");
    appContext.getBeanFactory().registerSingleton("solrServerStudinfo", solrServer);
    
    appContext.refresh();
    
    ApplicationContext bf = new ClassPathXmlApplicationContext(new String[] {
      "fsimportPropsContext.xml"
      ,"fsimportContext.xml"
      ,"studinfoMock.xml"
      ,"cpmock.xml"
      //,"solr-local.xml"
      ,"studinfoAnswerMockCurrentDate.xml"},
      appContext);
    
    StudInfoImport siImport = bf.getBean(StudInfoImport.class);
//    siImport.importStudyPrograms(217, 2011, "HØST", true, "N");
//    siImport.importCourses(217, 2011, "HØST", "N", true);
    siImport.importSubjects(217, 2011, "HØST", "N");
  }
  
  @Test
  public void resultsExist() throws SolrServerException {
    HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr");
    SolrParams params = new SolrQuery("cat:emne");
    QueryResponse response = server.query(params);
    int status = response.getStatus();
    assertThat(status, is(equalTo(0)));
    assertThat(response.getResults().getNumFound(), is(equalTo(1L)));
  }
}
