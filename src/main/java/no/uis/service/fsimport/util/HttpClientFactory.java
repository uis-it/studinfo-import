package no.uis.service.fsimport.util;

import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * Creates a HttpClient with preemptive Basic authentication. 
 */
public class HttpClientFactory implements FactoryBean {

  private String username;
  private String password;
  private URL url;

  @Required
  public void setUsername(String username) {
    this.username = username;
  }

  @Required
  public void setPassword(String password) {
    this.password = password;
  }

  @Required
  public void setUrl(URL url) {
    this.url = url;
  }
  
  @Override
  public Object getObject() throws Exception {
    DefaultHttpClient client = new DefaultHttpClient() {
      @Override
      protected HttpContext createHttpContext() {
        HttpContext context = super.createHttpContext();
        
        AuthCache authCache = new BasicAuthCache();
      
        BasicScheme basicAuth = new BasicScheme();
        HttpHost targetHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
        authCache.put(targetHost, basicAuth);
      
        context.setAttribute(ClientContext.AUTH_CACHE, authCache);
      
        return context;
      } 
    };
    BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(new AuthScope(url.getHost(), AuthScope.ANY_PORT), new UsernamePasswordCredentials(username, password));
    client.setCredentialsProvider(credsProvider);
    
    return client;
  }

  @Override
  public Class<HttpClient> getObjectType() {
    return HttpClient.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
