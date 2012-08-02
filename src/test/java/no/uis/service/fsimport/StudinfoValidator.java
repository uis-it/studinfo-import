package no.uis.service.fsimport;

import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import no.usit.fsws.wsdl.studinfo.StudInfoService;

import org.apache.cxf.helpers.IOUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class StudinfoValidator {

  private static final Integer INTEGER_1 = Integer.valueOf(1);
  private static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
  static private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StudinfoValidator.class);

  /**
   * @param args
   */
  public static void main(String[] args) {
    final StudinfoValidator validator = new StudinfoValidator();
    if (args.length != 2) {
      validator.printUsage();
    } else {
      try {
        validator.validate(args[0], args[1]);
      } catch(Exception ex) {
        log.error(null, ex);
      }
    }
  }

  private StudInfoService fsWsStudInfo;

  private void validate(String sYear, String semester) throws Exception {
    int year = Integer.parseInt(sYear);
    if (!semester.equals("VÅR") && !semester.equals("HØST")) {
      throw new IllegalArgumentException(semester);
    }
    
    validateCourses(semester, year);

    validatePrograms(semester, year);

    validateSubjects(semester, year);
}

  private void printMessages(List<String> messages) {
    for (String msg : messages) {
      System.out.println(msg);
    }
  }

  private void validateSubjects(String semester, int year) throws Exception {
    validateSubjects(217, year, semester, "B");
    validateSubjects(217, year, semester, "E");
    validateSubjects(217, year, semester, "N");
  }

  private void validatePrograms(String semester, int year) throws Exception {
    validateStudyPrograms(217, year, semester, "B");
    validateStudyPrograms(217, year, semester, "E");
    validateStudyPrograms(217, year, semester, "N");
  }

  private void validateCourses(String semester, int year) throws Exception {
    validateCourses(217, year, semester, "B");
    validateCourses(217, year, semester, "E");
    validateCourses(217, year, semester, "N");
  }

  private synchronized StudInfoService getBean() {
    if (fsWsStudInfo == null) {
      BeanFactory bf = new ClassPathXmlApplicationContext(new String[] {
        "fsStudInfoPropsContext.xml" 
       ,"fsimportContext.xml"
       });
  
      fsWsStudInfo = bf.getBean("fsWsStudInfo", StudInfoService.class);
    }
    return fsWsStudInfo;
  }

  private void printUsage() {
    System.out.println(this.getClass().getName() + " YEAR <VÅR | HØST>");
  }

  public void validateStudyPrograms(int institution, int year, String semester, String language)
      throws Exception
  {
    String studieinfoXml = getBean().getStudieprogramSI(year,
        semester, INTEGER_1, null, institution, INTEGER_MINUS_ONE, null, null, language);
    
    validate(studieinfoXml, ValidationErrorHandler.InfoType.studieprogram, year, semester, language);
  }

  private void validateSubjects(int institution, int year, String semester, String language) throws Exception {
    String studieinfoXml = getBean().getEmneSI(Integer.valueOf(institution), null, null,
      INTEGER_MINUS_ONE, null, null, year, semester, language);

    validate(studieinfoXml, ValidationErrorHandler.InfoType.emne, year, semester, language);
  }

  private void validateCourses(int institution, int year, String semester, String language)
      throws Exception
  {
    String studieinfoXml = getBean().getKursSI(Integer.valueOf(institution), INTEGER_MINUS_ONE, INTEGER_MINUS_ONE, INTEGER_MINUS_ONE, language);

    validate(studieinfoXml, ValidationErrorHandler.InfoType.kurs, year, semester, language);
  }

  protected void validate(String studieinfoXml, ValidationErrorHandler.InfoType infoType, int year, String semester, String language) throws Exception {
    
    // save xml
    File outFile = new File("target/out", infoType.toString() + year + semester + language + ".xml");
    if (outFile.exists()) {
      outFile.delete();
    } else {
      outFile.getParentFile().mkdirs();
    }
    final FileWriter outWriter = new FileWriter(outFile);
    IOUtils.copy(new StringReader(studieinfoXml) , outWriter, 1000);
    outWriter.flush();
    outWriter.close();
    
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating(false);
    factory.setNamespaceAware(true);
    
    SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
    Schema schema = schemaFactory.newSchema(new Source[] {new StreamSource(new File("src/main/xsd/studinfo.xsd"))});

    factory.setSchema(schema);
    
    SAXParser parser = factory.newSAXParser();
    
    XMLReader reader = parser.getXMLReader();
    ValidationErrorHandler errorHandler = new ValidationErrorHandler(infoType, year, semester, language);
    reader.setErrorHandler(errorHandler);
    reader.setContentHandler(errorHandler);
    try {
      reader.parse(new InputSource(new StringReader(studieinfoXml)));
    } catch(SAXException ex) {
      // do nothing. The error is handled in the error handler
    }
    printMessages(errorHandler.getMessages());
  }
  
}
