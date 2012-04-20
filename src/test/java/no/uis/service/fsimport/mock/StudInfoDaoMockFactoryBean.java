package no.uis.service.fsimport.mock;

import no.uis.service.model.studinfo.StudInfo;
import no.uis.service.studinfo.persistence.StudInfoDAO;

import org.easymock.EasyMock;
import org.springframework.beans.factory.FactoryBean;

public class StudInfoDaoMockFactoryBean implements FactoryBean {

  @Override
  public Object getObject() throws Exception {
    StudInfoDAO dao = EasyMock.createMock(StudInfoDAO.class);

    dao.saveXML(EasyMock.anyObject(StudInfo.class));
    EasyMock.expectLastCall().atLeastOnce();
    
    EasyMock.replay(dao);
    
    return dao;
  }

  @Override
  public Class<?> getObjectType() {
    return StudInfoDAO.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

}
