package no.uis.service.fsimport.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import no.uis.service.fsimport.FSImport;
import no.uis.service.fsimport.util.ImportReportUtil;
import no.uis.service.fsimport.util.ORMUtil;
import no.uis.service.mapping.FsBasdataMapper;
import no.uis.service.model.ImportReport;
import no.uis.service.model.Person;
import no.uis.service.model.StudyPlace;
import no.uis.service.model.StudyProgram;
import no.usit.fsws.schemas.basdata.Basdata;
import no.usit.fsws.schemas.basdata.Sted;
import no.usit.fsws.schemas.basdata.Steder;
import no.usit.fsws.schemas.basdata.StudentType;
import no.usit.fsws.schemas.basdata.StudieElementType;
import no.usit.fsws.schemas.cdm.CDM;
import no.usit.fsws.wsdl.bas.BASService;
import no.usit.fsws.wsdl.bas.EksportType;
import no.usit.fsws.wsdl.bas.FswsAttachment;
import no.usit.fsws.wsdl.cdm.CDMService;

import org.springframework.beans.factory.annotation.Required;

public class FSImportImpl implements FSImport {

	static private org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(FSImportImpl.class);

	private BASService fsService;
	private CDMService fsServiceCdm;
	private FsBasdataMapper fsMapper;
	private ORMUtil ormUtil;
	private boolean useAttachment;
	private boolean importStudyData;
	private boolean importPlaces;
  private Integer placesInstitution;

  // force full import on first run
  private boolean lastRunFailed = true;

	@Required
	public void setFsServiceCdm(CDMService fsServiceCdm) {
		this.fsServiceCdm = fsServiceCdm;
	}

	@Required
	public void setFsService(BASService fsService) {
		this.fsService = fsService;
	}

	@Required
	public void setFsMapper(FsBasdataMapper fsMapper) {
		this.fsMapper = fsMapper;
	}

	@Required
	public void setOrmUtil(ORMUtil ormUtil) {
		this.ormUtil = ormUtil;
	}

	public void setUseAttachment(boolean useAttachment) {
		this.useAttachment = useAttachment;
	}

	@Required
	public void setPlacesInstitution(Integer placesInstitution) {
    this.placesInstitution = placesInstitution;
  }

  public void setImportStudyData(boolean enable) {
	  this.importStudyData = enable;
	}

	public void setImportPlaces(boolean importPlaces) {
    this.importPlaces = importPlaces;
  }

  @Override
	public ImportReport importCdmFromFS() throws Exception {
    ImportReport report = ImportReportUtil.newImportReport("cdm");
	  if (importStudyData) {
	    log.info("Start: importCdmFromFS()");
  		String studieData = fsServiceCdm.getStudieData("bokmal");
  		// TODO this is an expensive operation, is there a way around it? 
  		studieData = studieData.replace("P1", "P1Y");
  		
  		@SuppressWarnings("unused")
      CDM cdm = parseCDM(studieData);
  		studieData = null;
  		log.info("Finished: importCdmFromFS()");
	  }
	  ImportReportUtil.stop(report);
	  
	  return report;
	}

  @Override
	public ImportReport importDataFromFS() throws Exception {

    ImportReport report = ImportReportUtil.newImportReport("bas");
		try {
  		if (lastRunFailed || ormUtil.isStudentAndLecturerEmpty()) {
  		  log.info("Start: importDataFromFS(FULL)");
  			importFSData(true, EksportType.FULL);
  		} else {
        log.info("Start: importDataFromFS(ENDRING)");
  			importFSData(true, EksportType.ENDRING);
  		}
  		this.lastRunFailed = false;
		} catch (Exception ex) {
		  this.lastRunFailed = true;
		  log.error("importDataFromFS", ex);
		  ImportReportUtil.add(report, ex);
		}
		log.info("Finished: importDataFromFS()");
		ImportReportUtil.stop(report);
		
		return report;
	}

  @Override
  public ImportReport importPlacesFromFS() throws Exception {
    ImportReport report = ImportReportUtil.newImportReport("sted");
    if (!importPlaces) {
      ImportReportUtil.add(report, "import disabled");
    } else {
      
      Integer institutionNo = Integer.valueOf(placesInstitution);
      Reader src = null;
      Steder steder = null;
      try {
        if (useAttachment) {
          FswsAttachment stedAsAttachment = fsService.getStedAsAttachment(institutionNo, null, null, null);
          src = new InputStreamReader(stedAsAttachment.getDataHandler().getInputStream());
        } else {
          String sted = fsService.getSted(institutionNo, null, null, null);
          src = new StringReader(sted);
        }
        steder = parseData(src, Steder.class);
        convertAndUpdate(steder);
      } finally {
        if (src != null) {
          src.close();
        }
      }
    }
    ImportReportUtil.stop(report);
    
    return report;
  }
  
  
  private void importFSData(boolean registerRun, EksportType exportType)
			throws Exception {
		Basdata basData = null;
		if (useAttachment) {
			log.info("Get Bas Data As Attachment");
			FswsAttachment basDataAsAttachment = fsService
					.getBASDataAsAttachment(exportType, registerRun);
			basData = parseBasDataAttachment(basDataAsAttachment);
		} else {
			log.info("Get Bas Data as String");
			String basDataString = fsService
					.getBASData(exportType, registerRun);
			basData = parseBasData(basDataString);
		}
		convertAndUpdate(basData);
		ormUtil.supplementPoliceCertRequirement();
	}

	private void convertAndUpdate(Basdata basData) {

	  if (log.isInfoEnabled()) {
	    log.info("Processing " + basData.getStudent().size() + " students");
	  }
		for (StudentType fsStudent : basData.getStudent()) {

			String studentNumber = String.valueOf(fsStudent.getStudentnr());
			Person targetStudent = ormUtil.getStudentForUpdate(studentNumber);

			if (targetStudent == null) {
			  targetStudent = new Person();
			}
			fsMapper.mapStudent(fsStudent, targetStudent);
			ormUtil.savePerson(targetStudent);
		}

		for (StudieElementType program : basData.getProgram()){
		  StudyProgram studyProgram = ormUtil.findStudyProgram(program.getId());
		  if (studyProgram == null) {
		    studyProgram = new StudyProgram();
		  }
			fsMapper.mapProgram(program, studyProgram);
			ormUtil.saveStudyProgram(studyProgram);
		}

		log.debug("Processing finished");
	}

	private void convertAndUpdate(Steder steder) {
	  if (steder == null) {
	    return;
	  }
	  
	  if (log.isDebugEnabled()) {
	    log.debug("Processing " + steder.getSted().size() + " places.");
	  }
	  
	  for (Sted sted : steder.getSted()) {
      String stedKode = sted.getKode();
      
      StudyPlace targetPlace = ormUtil.getPlaceForUpdate(stedKode);
      
      if (targetPlace == null) {
        targetPlace = new StudyPlace();
      }
      fsMapper.mapPlace(sted, targetPlace);
      ormUtil.savePlace(targetPlace);
    }
	}
	
	@SuppressWarnings("unchecked")
  private <T> T parseData(Reader inReader, Class<T> dataClass) throws JAXBException {
	  
	  JAXBContext jc = JAXBContext.newInstance(dataClass.getPackage().getName(), dataClass.getClassLoader());
	  
	  Unmarshaller um = jc.createUnmarshaller();
	  
	  return (T)um.unmarshal(inReader);
	}
	
  private CDM parseCDM(String studieData) throws JAXBException {

	  return parseData(new StringReader(studieData), CDM.class);
	}

	private Basdata parseBasDataAttachment(FswsAttachment basDataAsAttachment)
			throws JAXBException, IOException {

	  InputStreamReader inReader = new InputStreamReader(basDataAsAttachment.getDataHandler().getInputStream());
    return parseData(inReader, Basdata.class);
	}

	private Basdata parseBasData(String basData) throws JAXBException,
			IOException {

	  return parseData(new StringReader(basData), Basdata.class);
	}

}
