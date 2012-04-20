package no.uis.service.fsimport.util;

import java.io.FileReader;
import java.util.Properties;

import no.uis.service.fsimport.FSImport;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBImportUtil {

	private static final String[] CONFIGS_MOCK = new String[] {"classpath:fsimportContext.xml", "classpath:mock.xml", "classpath:personormContext.xml" };
	private static final String[] CONFIGS_NOMOCK = new String[] {"classpath:personormContext.xml", "classpath:fsJdbcClientContext.xml",	"classpath:fsimportContext.xml" };

	private final Properties properties;
	private final boolean noMock;

	private DBImportUtil(Properties props, boolean noMock) {
		properties = props;
		this.noMock = noMock;
	}

	public static void main(String[] args) {

		Options options = new Options();
		options.addOption(new Option("r", "db-url", true, "database url"));
		options.addOption(new Option("u", "username", true, "DB user"));
		options.addOption(new Option("p", "password", true, "DB password"));
		options.addOption(new Option("m", "mode", true,	"update mode: create-drop, update"));
		options.addOption(new Option("c", "driver", true, "DB driver class"));
		options.addOption(new Option("t", "type", true,	"Hibernate JPA DB type: POSTGRESQL, MYSQL, SQL_SERVER, HSQL"));
		options.addOption(new Option("d", "dialect", true, "Hibernate DB dialect"));
		options.addOption(new Option("f", "file", true, "property file"));
		options.addOption(new Option("x", "no-mock", false, "don't use mock"));

		try {
			CommandLineParser parser = new PosixParser();
			Properties props = new Properties();
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("f")) {
				FileReader fileReader = new FileReader(cmd.getOptionValue('f'));
				props.load(fileReader);
			} else {
				props.setProperty("database.driver.class.name", cmd
						.getOptionValue('c'));
				props.setProperty("database.url", cmd.getOptionValue('r'));
				props.setProperty("database.username", cmd.getOptionValue('u'));
				props.setProperty("database.password", cmd.getOptionValue('p'));
				props.setProperty("database.type", cmd.getOptionValue('t'));
				props.setProperty("hibernate.dialect", cmd.getOptionValue('d'));
				props.setProperty("hibernate.show.sql", "false");
				props.setProperty("fs.ws.use.attachment", "true");
				props.setProperty("fs.ws.import.places", "true");
				props.setProperty("fs.ws.places.institution", "217");
				props.setProperty("fs.ws.url", "http://localhost:9999/fs");
				props.setProperty("fs.ws.cdm.url", "http://localhost:9999/cdm");
				props.setProperty("fs.ws.username", "NOT_USED");
				props.setProperty("fs.ws.password", "NOT_USED");
				props.setProperty("fs.ws.connection.timeout", "300000");
				props.setProperty("fs.ws.receive.timeout", "600000");
				props.setProperty("example.user.nin", "00000000000");
				props.setProperty("hibernate.hbm2ddl.auto", cmd
						.getOptionValue('m'));
			}

			boolean noMock = cmd.hasOption('x');
			DBImportUtil util = new DBImportUtil(props, noMock);
			util.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println();
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("DBImportUtil", options);
			System.exit(1);
		}
	}

	private void execute() throws Exception {
		String[] configLocations = noMock ? CONFIGS_NOMOCK : CONFIGS_MOCK;
		ClassPathXmlApplicationContext xmlContext = new ClassPathXmlApplicationContext(
				configLocations, false, null);

		PropertyPlaceholderConfigurer propPlaceHolder = new PropertyPlaceholderConfigurer();
		propPlaceHolder.setProperties(properties);
		xmlContext.addBeanFactoryPostProcessor(propPlaceHolder);
		xmlContext.refresh();

		FSImport fsimport = (FSImport) xmlContext.getBean("fsImport");
		fsimport.importDataFromFS();
		fsimport.importPlacesFromFS();
	}
}
