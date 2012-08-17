package no.uis.service.fsimport;

import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import no.uis.service.fsimport.StudInfoImport.StudinfoType;
import no.usit.fsws.wsdl.studinfo.StudInfoService;

import org.apache.cxf.helpers.IOUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class StudinfoValidator {

  static private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StudinfoValidator.class);

  /**
   * @param args
   */
  public static void main(String[] args) {
    final StudinfoValidator validator = new StudinfoValidator();
    try {
      validator.validateMain(args);
    } catch(Exception ex) {
      log.error(null, ex);
    }
  }

  private StudInfoService fsWsStudInfo;

  private void validateMain(String[] args) throws Exception {
    if (args.length != 2) {
      printUsage();
    } else {
      List<String> messages = validateAll(Integer.parseInt(args[0]), args[1], null, null);
      printMessages(messages);
    }
  }
  
  public List<String> validateAll(int year, String semester, StudinfoType[] infoTypes, String[] langs) throws Exception {
    
    List<String> messages = new ArrayList<String>();
    
    for (StudinfoType infoType : infoTypes) {
      switch(infoType) {
        case KURS:
          messages.addAll(validateCourses(semester, year, langs));
          break;
        case STUDIEPROGRAM:
          messages.addAll(validatePrograms(semester, year, langs));
          break;
        case EMNE:
          messages.addAll(validateSubjects(semester, year, langs));
          break;
      }
    }

    
    return messages;
}

  private void printMessages(List<String> messages) {
    for (String msg : messages) {
      System.out.println(msg);
    }
  }

  private List<String> validateSubjects(String semester, int year, String[] langs) throws Exception {
    List<String> messages = new ArrayList<String>();
    for (String lang : langs) {
      messages.addAll(validateSubjects(217, year, semester, lang));
    }
    
    return messages;
  }

  private List<String> validatePrograms(String semester, int year, String[] langs) throws Exception {
    List<String> messages = new ArrayList<String>();
    for (String lang : langs) {
      messages.addAll(validateStudyPrograms(217, year, semester, lang));
    }
    
    return messages;
  }

  private List<String> validateCourses(String semester, int year, String[] langs) throws Exception {
    List<String> messages = new ArrayList<String>();
    for (String lang : langs) {
      messages.addAll(validateCourses(217, year, semester, lang));
    }
    return messages;
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

  public List<String> validateStudyPrograms(int institution, int year, String semester, String language)
      throws Exception
  {
    String studieinfoXml = getBean().getStudieprogramSI(year,
        semester, StudInfoImport.INTEGER_1, null, institution, StudInfoImport.INTEGER_MINUS_1, null, null, language);
    
    return validate(studieinfoXml, StudinfoType.STUDIEPROGRAM, year, semester, language);
  }

  private List<String> validateSubjects(int institution, int year, String semester, String language) throws Exception {
    String studieinfoXml = getBean().getEmneSI(Integer.valueOf(institution), null, null,
      StudInfoImport.INTEGER_MINUS_1, null, null, year, semester, language);

    return validate(studieinfoXml, StudinfoType.EMNE, year, semester, language);
  }

  private List<String> validateCourses(int institution, int year, String semester, String language)
      throws Exception
  {
    String studieinfoXml = getBean().getKursSI(Integer.valueOf(institution), StudInfoImport.INTEGER_MINUS_1, StudInfoImport.INTEGER_MINUS_1, StudInfoImport.INTEGER_MINUS_1, language);

    return validate(studieinfoXml, StudinfoType.KURS, year, semester, language);
  }

  protected List<String> validate(String studieinfoXml, StudinfoType infoType, int year, String semester, String language) throws Exception {
    
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
    return errorHandler.getMessages();
  }
  
}
