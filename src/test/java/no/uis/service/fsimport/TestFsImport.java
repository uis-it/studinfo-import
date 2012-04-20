package no.uis.service.fsimport;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import no.uis.service.fsimport.FSImport;
import no.uis.service.fsimport.util.ORMUtil;
import no.uis.service.model.StudyPlace;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestFsImport {

	private static FSImport fsImport;
	private static ORMUtil ormUtil;

	@BeforeClass
	public static void setup() {
//		BeanFactory bf = new ClassPathXmlApplicationContext(new String[] {
//				"fsimportPropsContext.xml", 
//				"fsimportContext.xml", 
//				"classpath:personormContext.xml", 
//				"classpath:fsJdbcClientContext.xml", 
//				"classpath:studinfoOrmContext.xml"});
//    BeanFactory bf = new ClassPathXmlApplicationContext(new String[] {
//      "fsimportPropsContext.xml", "fsimportContext.xml", "mock.xml" });
//    
//		fsImport = (FSImport) bf.getBean("fsImport", FSImport.class);
//		ormUtil = (ORMUtil) bf.getBean("ormUtil", ORMUtil.class);
	}

	//@Test
	public void runFullImport() throws Exception {
	  fsImport.importDataFromFS();
	  fsImport.importPlacesFromFS();
	}
	
	//@Test
	public void runSupplementPoliceCertRequirement(){
		ormUtil.supplementPoliceCertRequirement();
	}
	
	//@Test
	public void checkCDMService() throws Exception {
		fsImport.importCdmFromFS();
	}
	
	@SuppressWarnings("unchecked")
	//@Test
	public void checkPlacesImport() throws Exception {
	  fsImport.importPlacesFromFS();
	  StudyPlace place = ormUtil.findPlaceByPlaceRef("217_0_0_0");
	  assertThat(place, is(notNullValue()));
	  assertThat(place.getName(), is(notNullValue()));
	  assertThat(place.getName().size(), is(3));
	  assertThat(place.getName().get(0).getValue(), anyOf(is("University of Stavanger"), is("Universitetet i Stavanger")));
	}
	
	@Test
	public void emptyTest() {
	  
	}
}
