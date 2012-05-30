package no.uis.service.fsimport;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

import no.uis.service.fsimport.SolrTestRunner.SolrServer;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.easymock.internal.matchers.GreaterThan;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SolrTestRunner.class)
@SolrServer(home="src/test/resources/solr1", port = 8080, war = "target/dependency/solr.war")
public class UpdateSolrTest {

  @BeforeClass
  public static void init() {
    System.out.println("before Class");
  }
  
  @Before
  public void initTest() {
    System.out.println("before Test");
  }
  
  
  @AfterClass
  public static void destroy() {
    System.out.println("after Class");
  }
  
  @After
  public void destroyTest() {
    System.out.println("after Test");
  }
  
  
  @Test
  public void test() throws SolrServerException {
    HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr");
    SolrParams params = new SolrQuery("*:*");
    QueryResponse response = server.query(params);
    int status = response.getStatus();
    assertThat(status, is(equalTo(0)));
    assertThat(response.getQTime(), is(notNullValue()));
  }
}
