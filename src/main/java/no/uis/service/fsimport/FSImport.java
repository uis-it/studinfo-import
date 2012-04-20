package no.uis.service.fsimport;

import no.uis.service.model.ImportReport;


public interface FSImport {

  public ImportReport importDataFromFS() throws Exception;

  public ImportReport importCdmFromFS() throws Exception;

  public ImportReport importPlacesFromFS() throws Exception;
  
}