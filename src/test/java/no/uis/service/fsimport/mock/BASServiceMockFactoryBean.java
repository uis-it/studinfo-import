package no.uis.service.fsimport.mock;

import static org.easymock.EasyMock.*;

import java.net.URL;

import javax.activation.DataHandler;

import no.usit.fsws.wsdl.bas.BASService;
import no.usit.fsws.wsdl.bas.EksportType;
import no.usit.fsws.wsdl.bas.FswsAttachment;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

public class BASServiceMockFactoryBean implements FactoryBean {

  private URL fsStedResource;
  private URL fsBasdataResource;

  @Override
  public Object getObject() throws Exception {

    // Sted
    DataHandler stedDataHandler = EasyMock.createMock("dataHandler", DataHandler.class);
    expect(stedDataHandler.getInputStream()).andReturn(fsStedResource.openStream());
    replay(stedDataHandler);
    
    FswsAttachment stedFsAttachment = EasyMock.createMock("fsAttachment", FswsAttachment.class);
    expect(stedFsAttachment.getDataHandler()).andReturn(stedDataHandler);
    replay(stedFsAttachment);

    // Basdata
    DataHandler basdataDataHandler = EasyMock.createMock("dataHandler", DataHandler.class);
    expect(basdataDataHandler.getInputStream()).andReturn(fsBasdataResource.openStream());
    replay(basdataDataHandler);
    
    FswsAttachment basdataFsAttachment = EasyMock.createMock("fsAttachment", FswsAttachment.class);
    expect(basdataFsAttachment.getDataHandler()).andReturn(basdataDataHandler);
    replay(basdataFsAttachment);
    
    BASService fsBasService = EasyMock.createMock("basService", BASService.class);
    
    Capture<Integer> instNo = new Capture<Integer>();
    expect(fsBasService.getStedAsAttachment(capture(instNo), eq((Integer)null), eq((Integer)null), eq((Integer)null))).andReturn(stedFsAttachment);
    
    expect(fsBasService.getBASDataAsAttachment((EksportType)anyObject(), anyBoolean())).andReturn(basdataFsAttachment);
    
    replay(fsBasService);
    
    return fsBasService;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class getObjectType() {
    return BASService.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }

  @Required
  public void setFsStedResource(URL fsStedResource) {
    this.fsStedResource = fsStedResource;
  }

  @Required
  public void setFsBasdataResource(URL fsBasdataResource) {
    this.fsBasdataResource = fsBasdataResource;
  }
  
  
}
