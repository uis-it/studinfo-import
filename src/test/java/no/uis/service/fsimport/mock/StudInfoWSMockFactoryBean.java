package no.uis.service.fsimport.mock;

import java.util.Map;

import no.usit.fsws.wsdl.studinfo.StudInfoService;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.springframework.beans.factory.FactoryBean;
import static org.easymock.EasyMock.*;

public class StudInfoWSMockFactoryBean implements FactoryBean {

  private Map<String, AbstractResourceAnswer> answerMap;

  @Override
  public Object getObject() throws Exception {
    StudInfoService ws = EasyMock.createMock(StudInfoService.class);
    
    Capture<String> semester = new Capture<String>(); 
    Capture<String> language = new Capture<String>();
    
    // getEmneSI
    Integer institution = Integer.valueOf(217);
    Integer faculty = Integer.valueOf(-1);
    Integer year = Integer.valueOf(2011);
    IAnswer<String> emneAnswer = findAnswer("emner", language);
    expect(ws.getEmneSI(eq(institution), (String)eq(null), (String)eq(null), eq(faculty), (Integer)eq(null), (Integer)eq(null), eq(year), capture(semester), capture(language))).andAnswer(emneAnswer).atLeastOnce();
    
    // getStudieProgramSI
    language = new Capture<String>();
    IAnswer<String> programAnswer = findAnswer("studieprogram", language);
    expect(ws.getStudieprogramSI(anyObject(Integer.class), anyObject(String.class), anyObject(Integer.class), anyObject(String.class), eq(institution), eq(faculty), anyObject(Integer.class), anyObject(Integer.class), capture(language))).andAnswer(programAnswer).atLeastOnce();

    // getKursSI
    language = new Capture<String>();
    IAnswer<String> courseAnswer = findAnswer("kurs", language);
    expect(ws.getKursSI(eq(institution), anyObject(Integer.class), anyObject(Integer.class), anyObject(Integer.class), capture(language))).andAnswer(courseAnswer).atLeastOnce();
    EasyMock.replay(ws);
    return ws;
  }

  private IAnswer<String> findAnswer(String infoType, Capture<String> language) {
    IAnswer<String> emneAnswer = (answerMap != null ? answerMap.get(infoType) : null);
    if (emneAnswer == null) {
      emneAnswer = new StudInfoAnswer(infoType, language);
    }
    return emneAnswer;
  }

  @Override
  public Class<?> getObjectType() {
    return StudInfoService.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }
  
  public void setAnswers(Map<String, AbstractResourceAnswer> answers) {
    this.answerMap = answers; 
  }
}
