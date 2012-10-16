package no.uis.service.fsimport;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import no.uis.service.fsimport.util.Studinfos;
import no.uis.service.studinfo.data.FsSemester;
import no.uis.service.studinfo.data.FsYearSemester;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FsYearSemesterTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  @Test
  public void testInstantiation() throws Exception {
    FsYearSemester ys = FsYearSemester.valueOf("2006H");
    
    assertThat(ys, is(notNullValue()));
    assertThat(ys.getYear(), is(2006));
    assertThat(ys.getSemester(), is(FsSemester.HOST));
  }
  
  @Test
  public void failedInstantiation() {
    thrown.expect(IllegalArgumentException.class);
    FsYearSemester.valueOf("2003");
    fail("Should have cast an exception");
  }
  
  @Test
  public void nullInstantiation() {
    FsYearSemester v = FsYearSemester.valueOf(null);
    assertThat(v, is(nullValue()));
  }
  
  @Test
  public void emptyInstantiation() {
    FsYearSemester v = FsYearSemester.valueOf(" ");
    assertThat(v, is(nullValue()));
  }
  
  @Test
  public void testDiffSemesters1() throws Exception {
    FsYearSemester ys1 = FsYearSemester.valueOf("2009V");
    FsYearSemester ys2 = FsYearSemester.valueOf("2008H");
    
    int diffSemesters = Studinfos.getDiffSemesters(ys1, ys2);
    assertThat(diffSemesters, is(1));
  }

  @Test
  public void testDiffSemestersM1() throws Exception {
    FsYearSemester ys1 = FsYearSemester.valueOf("2008H");
    FsYearSemester ys2 = FsYearSemester.valueOf("2009V");
    
    int diffSemesters = Studinfos.getDiffSemesters(ys1, ys2);
    assertThat(diffSemesters, is(-1));
  }

  @Test
  public void testDiffSemestersEqual() throws Exception {
    FsYearSemester ys1 = FsYearSemester.valueOf("2009V");
    FsYearSemester ys2 = FsYearSemester.valueOf("2009V");
    
    int diffSemesters = Studinfos.getDiffSemesters(ys1, ys2);
    assertThat(diffSemesters, is(0));
  }

  @Test
  public void testDiffSemesters3() throws Exception {
    FsYearSemester ys1 = FsYearSemester.valueOf("2009V");
    FsYearSemester ys2 = FsYearSemester.valueOf("2007H");
    
    int diffSemesters = Studinfos.getDiffSemesters(ys1, ys2);
    assertThat(diffSemesters, is(3));
  }
}
