package no.uis.service.fsimport.mock;

import org.springframework.beans.factory.FactoryBean;

import com.corepublish.api.Accessor;

public class XmlAccessorFactoryBean implements FactoryBean {

  @Override
  public Object getObject() throws Exception {
    return null;
  }

  @Override
  public Class<?> getObjectType() {
    return Accessor.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }
}
