package no.uis.service.fsimport.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import no.uis.service.client.jdbc.dao.FsJdbcDAO;
import no.usit.fsws.wsdl.studinfo.StudInfoService;

import org.springframework.util.FileCopyUtils;

public class StudInfoServiceJDBCAdapter implements StudInfoService {

  private FsJdbcDAO fsDao;
  
  @Override
  public String getEksamenSI(Integer institusjonsnr, Integer faknr, Integer instituttnr, Integer gruppenr, Integer arstall,
      String terminkode, String sprak)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getEmneSI(Integer institusjonsnr, String emnekode, String versjonskode, Integer faknr, Integer instituttnr,
      Integer gruppenr, Integer arstall, String terminkode, String sprak)
  {
    String xmlFileName = fsDao.importSubjects(institusjonsnr.intValue(), emnekode, versjonskode, toSIValue(faknr), toSIValue(instituttnr), toSIValue(gruppenr), arstall, terminkode, sprak);
    return copyFileToString(xmlFileName);
  }

  @Override
  public String getFagpersonSI(Integer arstall, String terminkode, Integer fodselsdato, Integer personnr, Integer institusjonsnr,
      Integer faknr, Integer instituttnr, Integer gruppenr, String sprak)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getKodeSI(Integer arstall, String terminkode, String sprak) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getKursSI(Integer institusjonsnr, Integer faknr, Integer instituttnr, Integer gruppenr, String sprak) {
    String xmlFileName = fsDao.importCourses(toSIValue(institusjonsnr), toSIValue(faknr), toSIValue(instituttnr), toSIValue(gruppenr), sprak);
    return copyFileToString(xmlFileName);
  }

  @Override
  public String getStedSI(Integer institusjonsnr, Integer faknr, Integer instituttnr, Integer gruppenr, Integer arstall,
      String terminkode, String sprak)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getStudieprogramSI(Integer arstall, String terminkode, Integer medUPinfo, String studieprogramkode,
      Integer institusjonsnr, Integer faknr, Integer instituttnr, Integer gruppenr, String sprak)
  {
    String xmlFileName = fsDao.importStudyProgram(arstall.intValue(), terminkode, medUPinfo.intValue(), studieprogramkode, toSIValue(institusjonsnr), toSIValue(faknr), toSIValue(instituttnr), toSIValue(gruppenr), sprak);
    return copyFileToString(xmlFileName);
  }

  @Override
  public String getStudieretningSI(Integer arstall, String terminkode, String studieretningkode, Integer institusjonsnr,
      Integer faknr, Integer instituttnr, Integer gruppenr, String sprak)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getUndervisningSI(Integer institusjonsnr, Integer faknr, Integer instituttnr, Integer gruppenr, Integer arstall,
      String terminkode, String emnekode, String versjonskode, Integer fraStudieNiva, Integer tilStudieNiva, String sprak)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public void setFsDao(FsJdbcDAO fsDao) {
    this.fsDao = fsDao;
  }
  
  private static int toSIValue(Integer in) {
    return (in == null) ? -1 : in.intValue(); 
  }

  private String copyFileToString(String xmlFileName) {
    try {
      Reader xmlReader = new InputStreamReader(new FileInputStream(xmlFileName), "ISO-8859-1");
      return FileCopyUtils.copyToString(xmlReader);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
}
