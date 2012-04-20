package no.uis.service.fsimport.mock;

import static org.easymock.EasyMock.*;

import java.util.Map;

import no.uis.service.model.Person;
import no.uis.service.model.StudyPlace;
import no.uis.service.model.StudyProgram;
import no.uis.service.model.TypeBase;
import no.uis.service.persistence.PersonDAO;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.springframework.beans.factory.FactoryBean;

public class PersonDAOMockFactoryBean implements FactoryBean {

  private Person person;
  private StudyProgram studyProgram;
  private Map<String, StudyPlace> placesMap;

  public void setPerson(Person person) {
    this.person = person;
  }

  public void setStudyProgram(StudyProgram studyProgram) {
    this.studyProgram = studyProgram;
  }

  public void setPlacesMap(Map<String, StudyPlace> placesMap) {
    this.placesMap = placesMap;
  }

  @Override
  public Object getObject() throws Exception {
    PersonDAO personDao = EasyMock.createMock(PersonDAO.class);
    
    Capture<String> nin = new Capture<String>();
    
    expect(personDao.findPersonByNIN(capture(nin))).andReturn(person).atLeastOnce();
    
    expect(personDao.findStudyProgramByProgramId((String)anyObject())).andReturn(studyProgram).atLeastOnce();
    
    final Capture<Person> personCap = new Capture<Person>();
    IAnswer<? extends Person> savePersonAnswer = new IAnswer<Person>() {
      @Override
      public Person answer() throws Throwable {
        return personCap.getValue();
      }
    };
    
    expect(personDao.savePerson(capture(personCap))).andAnswer(savePersonAnswer).anyTimes();
    
    Capture<String> placeRef = new Capture<String>();
    IAnswer<? extends StudyPlace> answer = new FindStudyPlaceAnswer(placeRef, placesMap);
    expect(personDao.findPlaceByPlaceRef(capture(placeRef))).andAnswer(answer).atLeastOnce();
    
    final Capture<StudyPlace> placeCapture = new Capture<StudyPlace>();
    IAnswer<? extends StudyPlace> savePlaceAnswer = new IAnswer<StudyPlace>(){

      @Override
      public StudyPlace answer() throws Throwable {
        return placeCapture.getValue();
      }
    };
    expect(personDao.saveStudyPlace(capture(placeCapture))).andAnswer(savePlaceAnswer).atLeastOnce();

    expect(personDao.getStudentAndLecturerCount()).andReturn(Long.valueOf(1));
    
    expect(personDao.findPersonByStudentNumber((String)anyObject())).andReturn(person).atLeastOnce();

    final Capture<StudyProgram> programCapture = new Capture<StudyProgram>();
    IAnswer<? extends StudyProgram> saveProgramAnswer = new IAnswer<StudyProgram>() {
      @Override
      public StudyProgram answer() throws Throwable {
        return programCapture.getValue();
      }
    };
    expect(personDao.saveStudyProgram(capture(programCapture))).andAnswer(saveProgramAnswer).anyTimes();
    
    personDao.deleteInstance((TypeBase)anyObject());
    expectLastCall().atLeastOnce();
    
    replay(personDao);
    
    return personDao;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class getObjectType() {
    return PersonDAO.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
