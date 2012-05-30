package no.uis.service.fsimport;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * A Junit4 Test Runner that starts a jetty server before test initialization ({@code @BeforeClass}) and stops it after all tests ({@code @AfterClass}).
 * The Testrunner has also {@link #startSolrServer(String, int, String)} to create and start a jetty server prgrammatically. 
 * {@link #stopSolrServer(Server)} stops the server.
 * 
 * <h1>Example:</h1><blockquote><pre>
 * &#64;RunWith(SolrTestRunner.class)
 * &#64;SolrServer(home="src/test/resources/solr1", port = 8080, war = "target/dependency/solr.war")
 * public class UpdateSolrTest {
 *
 *   &#64;BeforeClass
 *   public static void init() {
 *     System.out.println("before Class");
 *   }
 *   
 *   &#64;Before
 *   public void initTest() {
 *     System.out.println("before Test");
 *   }
 * 
 *   &#64;AfterClass
 *   public static void destroy() {
 *     System.out.println("after Class");
 *   }
 *   
 *   &#64;After
 *   public void destroyTest() {
 *     System.out.println("after Test");
 *   }
 * 
 *   &#64;Test
 *   public void test() {
 *     System.out.println("Run Test");
 *     assertTrue(true);
 *   }
 * }</pre></blockquote>
 * 
 * The POM should contain:
 * <pre><blockquote>
 *    &lt;dependency>
 *      &lt;groupId>org.mortbay.jetty&lt;/groupId>
 *      &lt;artifactId>jetty&lt;/artifactId>
 *     &lt;version>6.1.26&lt;/version>
 *     &lt;scope>test&lt;/scope>
 *   &lt;/dependency>
 *   &lt;dependency>
 *     &lt;groupId>org.mortbay.jetty&lt;/groupId>
 *     &lt;artifactId>jsp-2.1&lt;/artifactId>
 *     &lt;version>6.1.14&lt;/version>
 *     &lt;scope>test&lt;/scope>
 *   &lt;/dependency>
 * </pre></blockquote>
 *  
 * @author Martin Goldhahn 
 */
public class SolrTestRunner extends BlockJUnit4ClassRunner {

  private Server jetty;
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  public @interface SolrServer {
    public String home();
    public int port();
    public String war();
  }
  
  public SolrTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }

  @Override
  protected Statement withBeforeClasses(Statement statement) {
    return new SolrStart(super.withBeforeClasses(statement));
  }

  @Override
  protected Statement withAfterClasses(Statement statement) {
    return new SolrStop(super.withAfterClasses(statement));
  }

  public static Server startSolrServer(String home, int port, String webapp) throws Exception {
    Server server = createSolrServer(home, port, webapp);
    startServer(server);
    
    return server;
  }
  
  public static void stopSolrServer(Server server) throws Exception {
    stopServer(server);
  }
  
  private static Server createSolrServer(String home, int port, String war) {
    File fHome = new File(home);
    System.setProperty("solr.solr.home", fHome.getAbsolutePath());
    
    Server server = new Server();
    SocketConnector connector = new SocketConnector();
    connector.setMaxIdleTime(3600);
    connector.setSoLingerTime(-1);
    connector.setPort(port);
    server.setConnectors(new Connector[] {connector});
    
    WebAppContext wac = new WebAppContext();
    wac.setServer(server);
    wac.setContextPath("/solr");
    
    wac.setWar(war);
    server.addHandler(wac);
    
    return server;
  }
  
  private void startSolrServer() {
    Class<?> javaClass = getTestClass().getJavaClass();
    SolrServer aSolrServer = javaClass.getAnnotation(SolrServer.class);
    String home;
    int port;
    String war;
    if (aSolrServer == null) {
      home = "src/test/resources/solr";
      port = 8080;
      war = "target/dependency/solr.war";
    } else {
      home = aSolrServer.home();
      port = aSolrServer.port();
      war = aSolrServer.war();
    }
  
    try {
      this.jetty = createSolrServer(home, port, war);
      startServer(this.jetty);
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void stopSolrServer() {
    try {
      stopServer(this.jetty);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private static void startServer(Server server) throws Exception {
      System.out.println("Starting embedded jetty server");
      server.start();
  }
  
  private static void stopServer(Server server) throws Exception {
      server.stop();
      server.join();
  }
  
  protected class SolrStart extends Statement {
    
    private Statement nextStatement;

    public SolrStart(Statement nextStatement) {
      this.nextStatement = nextStatement;
    }
    
    @Override
    public void evaluate() throws Throwable {
      startSolrServer();
      nextStatement.evaluate();
    }
  }
  
  protected class SolrStop extends Statement {

    private Statement previousStatement;

    public SolrStop(Statement previousStatement) {
      this.previousStatement = previousStatement;
    }
    
    @Override
    public void evaluate() throws Throwable {
      previousStatement.evaluate();
      stopSolrServer();
    }
    
  }
}
