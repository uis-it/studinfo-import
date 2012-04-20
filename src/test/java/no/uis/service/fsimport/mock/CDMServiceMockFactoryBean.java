package no.uis.service.fsimport.mock;

import no.usit.fsws.wsdl.cdm.CDMService;

import org.easymock.EasyMock;
import org.springframework.beans.factory.FactoryBean;

public class CDMServiceMockFactoryBean implements FactoryBean {

  @Override
  public Object getObject() throws Exception {
    CDMService cdmMock = EasyMock.createMock(CDMService.class);
    
    EasyMock.replay(cdmMock);
    return cdmMock;
  }

  @Override
  public Class<?> getObjectType() {
    return CDMService.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
