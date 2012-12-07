package no.uis.service.fsimport;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import no.uis.service.fsimport.impl.AbstractStudinfoImport;
import no.uis.service.fsimport.impl.StudInfoImportImpl;
import no.uis.service.fsimport.util.Studinfos;
import no.uis.service.studinfo.data.FsSemester;
import no.uis.service.studinfo.data.FsStudieinfo;
import no.uis.service.studinfo.data.FsYearSemester;
import no.uis.service.studinfo.data.Studieprogram;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class StudinfosTest {

  private Resource testData;
  private Resource transformer;
  private ResourceLoader loader = new DefaultResourceLoader();
  
  @Before
  public void init() throws Exception {
    testData = loader.getResource("file:src/test/data/b-data-future.xml");
    assumeNotNull(testData);
    
    transformer = loader.getResource("classpath:/fspreprocess.xsl");
    assumeNotNull(transformer);
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void test() throws Exception {
    AbstractStudinfoImport importer = new ImportMock();
    importer.setTransformerUrl(transformer.getURL());
    
    FsStudieinfo sinfo = importer.fetchStudyPrograms(217, 2013, "VÅR", true, "B");

    assumeNotNull(sinfo);
    
    assumeThat(sinfo.getStudieprogram(), hasItems(notNullValue(Studieprogram.class)));
    
    Studieprogram bdata = sinfo.getStudieprogram().get(0);
    assumeThat(bdata.getStudieprogramkode(), is("B-DATA"));
    
    Studinfos.cleanUtdanningsplan("B-DATA", bdata.getUtdanningsplan(), new FsYearSemester(2013, FsSemester.VAR), 6);
    
    assertThat(bdata.getUtdanningsplan().getKravSammensetting().size(), is(3));
  }
  
  private class ImportMock extends AbstractStudinfoImport {
    @Override
    protected Reader fsGetStudieprogram(int institution, int year, String semester, boolean includeEP, String language) {
      try {
        return new InputStreamReader(testData.getInputStream(), "ISO-8859-1");
      } catch(Exception e) {
        assumeNoException(e);
      }
      return null;
    }
    
    @Override
    protected Reader fsGetEmne(int institution, int year, String semester, String language) {
      throw new UnsupportedOperationException();
    }
    
    @Override
    protected Reader fsGetKurs(int institution, String language) {
      throw new UnsupportedOperationException();
    }
  }
}

