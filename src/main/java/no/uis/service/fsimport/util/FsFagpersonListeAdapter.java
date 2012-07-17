package no.uis.service.fsimport.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import no.uis.service.studinfo.data.FagpersonListe;
import no.uis.service.studinfo.data.FsFagperson;
import no.uis.service.studinfo.data.Personnavn;

public class FsFagpersonListeAdapter {

  public static List<FsFagperson> unmarshal(FagpersonListe fagpersonListe) {
    
    if (fagpersonListe == null || !fagpersonListe.isSetPersonidAndFnrAndPersonrolle()) {
      return Collections.emptyList();
    }
    
    List<JAXBElement<?>> personElements = fagpersonListe.getPersonidAndFnrAndPersonrolle();
    
    List<Map<String,Object>> pmapList = new ArrayList<Map<String, Object>>(personElements.size() / 5);
    Map<String, Object> pmap = new HashMap<String, Object>();
    
    for (JAXBElement<?> element : personElements) {
      String key = element.getName().getLocalPart();
      Object value = element.getValue();
      // personid is always the first element of the sequence, we store the current pmap in the list and create a new pmap
      if (key.equals("personid") && pmap.containsKey("personid")) {
        pmapList.add(pmap);
        pmap = new HashMap<String, Object>();
      }
      pmap.put(key, value);
    }
    pmapList.add(pmap);

    List<FsFagperson> personList = new ArrayList<FsFagperson>(pmapList.size());
    for (Map<String,Object> map : pmapList) {
      personList.add(pmapToPerson(map));
    }
    
    return personList;
  }

  private static FsFagperson pmapToPerson(Map<String, Object> pmap) {
    FsFagperson person = new FsFagperson();
    person.setPersonId(pmap.get("personid").toString());
    person.setFnr(pmap.get("fnr").toString());
    person.setPersonnavn((Personnavn)pmap.get("personnavn"));
    person.setPersonrolle(pmap.get("personrolle").toString());
    person.setDatoFra((Calendar)pmap.get("datofra"));
    person.setDatoTil((Calendar)pmap.get("datotil"));
    return person;
  }
}
