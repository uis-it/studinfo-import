package no.uis.service.fsimport.mock;

import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;
import org.springframework.beans.factory.FactoryBean;
import static org.easymock.EasyMock.*;

public class SolrServerFactoryBean implements FactoryBean {

  @Override
  public Object getObject() throws Exception {
    SolrServer mock = createMock(SolrServer.class);
    
    UpdateResponse deleteResult = new UpdateResponse();
    expect(mock.deleteByQuery(anyObject(String.class))).andReturn(deleteResult).anyTimes();
    expect(mock.request(anyObject(SolrRequest.class))).andReturn(null).anyTimes();
    expect(mock.commit()).andReturn(null).anyTimes();
    QueryResponse queryResponse = createMock(QueryResponse.class);

    SolrDocumentList docs = createMock(SolrDocumentList.class);
    expect(queryResponse.getResults()).andReturn(docs).anyTimes();
    
    expect(mock.query(anyObject(SolrParams.class))).andReturn(queryResponse).anyTimes();
    
    expect(docs.isEmpty()).andReturn(Boolean.FALSE).anyTimes();
    
    replay(mock, queryResponse, docs);
    return mock;
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
