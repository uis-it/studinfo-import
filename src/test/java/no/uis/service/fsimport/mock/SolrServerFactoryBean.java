package no.uis.service.fsimport.mock;

import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.FactoryBean;

public class SolrServerFactoryBean implements FactoryBean {

  @Override
  public Object getObject() throws Exception {
//    SolrServer mock = createMock(SolrServer.class);
//    
//    UpdateResponse deleteResult = new UpdateResponse();
//    expect(mock.deleteByQuery(eq("category:studinfo_kurs"))).andReturn(deleteResult).once();
//
//    
//    replay(mock);
//    return mock;
    return null;
  }

  @Override
  public Class<?> getObjectType() {
    return SolrServer.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }

}
