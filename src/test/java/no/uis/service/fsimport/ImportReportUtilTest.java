package no.uis.service.fsimport;

import org.junit.Test;

import no.uis.service.fsimport.util.ImportReportUtil;
import no.uis.service.model.ImportReport;
import no.uis.service.model.ImportReportEntry;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.junit.Assert.*;


public class ImportReportUtilTest {

  @Test
  public void addReport() {
    ImportReport r = ImportReportUtil.newImportReport("test");
    ImportReportUtil.add(r, "test");
    ImportReportUtil.stop(r);
    
    assertThat(r, is(notNullValue()));
  }
  
  @Test
  public void checkCombine() {
    ImportReport r1 = ImportReportUtil.newImportReport("test1");
    ImportReportUtil.add(r1, "test1");
    ImportReportUtil.stop(r1);

    ImportReport r2 = ImportReportUtil.newImportReport("test2");
    ImportReportUtil.add(r2, "test2");
    ImportReportUtil.stop(r2);
    
    ImportReport r = ImportReportUtil.combine(r1, r2);
    
    assertThat(r.getEntry(), hasItem(notNullValue(ImportReportEntry.class)));
  }
}
