package no.uis.service.fsimport.impl;

import java.io.Reader;
import java.io.StringReader;

import no.usit.fsws.wsdl.studinfo.StudInfoService;

public class StudInfoImportImpl extends AbstractStudinfoImport {

  private StudInfoService fsServiceStudInfo;
  public void setFsServiceStudInfo(StudInfoService fsServiceStudInfo) {
    this.fsServiceStudInfo = fsServiceStudInfo;
  }

  public StudInfoService getFsServiceStudInfo() {
    return this.fsServiceStudInfo;
  }

  @Override
  protected Reader fsGetStudieprogram(int institution, int year, String semester, boolean includeEP, String language) {
    Integer medUPinfo = includeEP ? INTEGER_1 : INTEGER_0;
    String studieinfoXml = fsServiceStudInfo.getStudieprogramSI(year,
      semester, medUPinfo, null, institution, INTEGER_MINUS_1, null, null, language);
    return new StringReader(studieinfoXml);
  }
  
  @Override
  protected Reader fsGetEmne(int institution, int year, String semester, String language) {
    String studieinfoXml = fsServiceStudInfo.getEmneSI(Integer.valueOf(institution), null, null,
      INTEGER_MINUS_1, null, null, year, semester, language);
    return new StringReader(studieinfoXml);
  }
  
  @Override
  protected Reader fsGetKurs(int institution, String language) {
    String studieinfoXml = fsServiceStudInfo.getKursSI(Integer.valueOf(institution), INTEGER_MINUS_1, INTEGER_MINUS_1, INTEGER_MINUS_1, language);
    return new StringReader(studieinfoXml);
  }
}
