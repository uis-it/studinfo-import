package no.uis.service.fsimport.mock;

import java.util.Map;

import no.uis.service.model.StudyPlace;

import org.easymock.Capture;
import org.easymock.IAnswer;

public class FindStudyPlaceAnswer implements IAnswer<StudyPlace> {

  
  private Map<String, StudyPlace> placesMap;
  private Capture<String> studyPlace;

  public FindStudyPlaceAnswer(Capture<String> studyPlace, Map<String, StudyPlace> placesMap) {
    this.studyPlace = studyPlace;
    this.placesMap = placesMap;
  }
  
  @Override
  public StudyPlace answer() throws Throwable {
    return placesMap.get(studyPlace.getValue());
  }
}
