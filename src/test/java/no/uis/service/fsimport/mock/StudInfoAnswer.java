package no.uis.service.fsimport.mock;


import org.easymock.Capture;

public class StudInfoAnswer extends AbstractResourceAnswer {

  String studInfoType;
  Capture<String> language;
  
  public StudInfoAnswer(String studInfoType, Capture<String> language) {
    this.studInfoType = studInfoType;
    this.language = language;
  }

  @Override
  protected String getResourceName() {
    String langCode = language.getValue().substring(0, 1);
    String resourceName = String.format("%s_%s.xml", studInfoType, langCode.toLowerCase());

    return resourceName;
  }
}
