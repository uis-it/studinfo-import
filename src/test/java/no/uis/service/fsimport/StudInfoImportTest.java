package no.uis.service.fsimport;

import static org.junit.Assert.assertTrue;
import no.uis.service.fsimport.StudInfoImport;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StudInfoImportTest {

	private static StudInfoImport studInfoImport;

	@BeforeClass
	public static void setup() {
		BeanFactory bf = new ClassPathXmlApplicationContext(new String[] {
				 "fsimportPropsContext.xml" 
				,"fsimportContext.xml"
				,"studinfoMock.xml"
				,"studinfoAnswerMockLarge.xml"
				,"cpmock.xml"
				,"solrMock.xml"
				});

		studInfoImport = (StudInfoImport) bf.getBean("fsStudInfoImport",
				StudInfoImport.class);
	}

//	@Test
	public void getSubjects() throws Exception {
		studInfoImport.importSubjects(217, 2011, "VÅR", "B");
		studInfoImport.importSubjects(217, 2011, "VÅR", "N");
		studInfoImport.importSubjects(217, 2011, "VÅR", "E");
		assertTrue(true);
	}

	// @Test
	public void getStudyPrograms() throws Exception {
//		studInfoImport.importStudyPrograms(217, 2011, "VÅR", false, "B");
//		studInfoImport.importStudyPrograms(217, 2011, "VÅR", false, "N");
		studInfoImport.importStudyPrograms(217, 2011, "VÅR", false, "E");
    assertTrue(true);
	}

	@Test
	public void getKurs() throws Exception {
	  studInfoImport.importCourses(217, 2011, "VÅR", "B", true);
    assertTrue(true);
	}
}
