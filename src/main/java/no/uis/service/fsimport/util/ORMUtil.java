package no.uis.service.fsimport.util;

import java.util.List;

import no.uis.service.client.jdbc.dao.FsJdbcDAO;
import no.uis.service.model.AffiliationData;
import no.uis.service.model.BaseText;
import no.uis.service.model.Contact;
import no.uis.service.model.Person;
import no.uis.service.model.StudyPlace;
import no.uis.service.model.StudyProgram;
import no.uis.service.persistence.PersonDAO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ORMUtil {

	private Logger log = Logger.getLogger(ORMUtil.class); 
	
	private PersonDAO personDao;
	private FsJdbcDAO fsJdbcDao;

	@Required
	public void setPersonDao(PersonDAO personDao) {
		this.personDao = personDao;
	}
	
	@Required
	public void setFsJdbcDao(FsJdbcDAO fsJdbcDao) {
		this.fsJdbcDao = fsJdbcDao;
	}

	/**
	 * Remove affiliations and contacts for existing person objects. 
	 * @param studentNumber
	 * @return null if not found
	 */
	public Person getStudentForUpdate(String studentNumber) {

		Person localPerson = null;
		try {
			localPerson = personDao.findPersonByStudentNumber(studentNumber);
			if (localPerson == null) {
				return null;
			}

			this.removeAffiliations(localPerson);
			this.removeContacts(localPerson);

		} catch (Exception e) {
		  log.warn("getStudentForUpdate", e);
		}
		return localPerson;
	}

	private void removeAffiliations(Person person) {
		for (AffiliationData afd : person.getAffiliationData()) {
			personDao.deleteInstance(afd);
		}
		person.getAffiliationData().clear();
	}

	private void removeContacts(Person person) {
		List<Contact> contacts = person.getContact();
		for (Contact contact : contacts) {
			personDao.deleteInstance(contact);
		}
		person.getContact().clear();
	}
	
	public void savePerson(Person person){
		personDao.savePerson(person);
	}
	
	public boolean isStudentAndLecturerEmpty(){
		Long count = personDao.getStudentAndLecturerCount();
		return count.longValue() == 0L;
	}
	
	public void saveStudyProgram(StudyProgram program){
	  personDao.saveStudyProgram(program);
	}

	public StudyProgram findStudyProgram(String progId) {
	  return personDao.findStudyProgramByProgramId(progId);
	}
	
  public StudyPlace findPlaceByPlaceRef(String placeRef) {
    return personDao.findPlaceByPlaceRef(placeRef);
  }

  public void savePlace(StudyPlace place) {
    personDao.saveStudyPlace(place);
  }

  /**
   * Remove the name texts prior to updating the StudyPlace.
   * 
   * @param placeRef
   * @return null if no StudyPlace was found.
   */
  public StudyPlace getPlaceForUpdate(String placeRef) {
    StudyPlace place = findPlaceByPlaceRef(placeRef);
    if (place != null) {
      for (BaseText text : place.getName()) {
        personDao.deleteInstance(text);
      }
      place.getName().clear();
    }
    return place;
  }
  
  /**
   * Update study program with info about police certificate requirements
   *
   */
  public void supplementPoliceCertRequirement(){
	  List<String> studyPrograms = fsJdbcDao.getPoliceCertRequiredStudyPrograms();
	  
	  for (String id : studyPrograms){
		  StudyProgram persistedProgram = personDao.findStudyProgramByProgramId(id);
		  if (persistedProgram != null) {
			  persistedProgram.setPoliceCertificateRequired(true);
		  }
	  }
  }
}
