package no.uis.service.fsimport.mock;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.cxf.helpers.IOUtils;
import org.easymock.IAnswer;

public abstract class AbstractResourceAnswer implements IAnswer<String> {

  @Override
  public String answer() throws Throwable {
    String resName = getResourceName();
    if (resName == null) {
      return "";
    }
    
    InputStream resultStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resName);
    InputStreamReader ir = new InputStreamReader(resultStream, IOUtils.UTF8_CHARSET);
    
    String result = IOUtils.toString(ir);
    int start = result.indexOf("<fs-studieinfo");
    return result.substring(start);
  }

  protected abstract String getResourceName();
}