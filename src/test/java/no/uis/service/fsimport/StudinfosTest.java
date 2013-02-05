package no.uis.service.fsimport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.io.InputStreamReader;
import java.io.Reader;

import no.uis.service.fsimport.impl.AbstractStudinfoImport;
import no.uis.service.fsimport.impl.SkippingAmpersandParser;
import no.uis.service.fsimport.util.Studinfos;
import no.uis.service.studinfo.data.FsSemester;
import no.uis.service.studinfo.data.FsStudieinfo;
import no.uis.service.studinfo.data.FsYearSemester;
import no.uis.service.studinfo.data.Studieprogram;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

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
    importer.setXmlSourceParser(SkippingAmpersandParser.class.getName());
    
    FsStudieinfo sinfo = importer.fetchStudyPrograms(217, 2013, FsSemester.VAR.toString(), true, "B");

    assumeNotNull(sinfo);
    
    assumeThat(sinfo.getStudieprogram(), hasItems(notNullValue(Studieprogram.class)));
    
    Studieprogram bdata = sinfo.getStudieprogram().get(0);
    assumeThat(bdata.getStudieprogramkode(), is("B-DATA"));
    
    Studinfos.cleanUtdanningsplan(bdata.getUtdanningsplan(), "B-DATA", new FsYearSemester(2013, FsSemester.VAR), 6);
    
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

