package no.uis.service.fsimport.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;

import no.uis.service.model.ImportReport;
import no.uis.service.model.ImportReportEntry;

public class ImportReportUtil {

  public static ImportReport newImportReport(String prefix) {
    ImportReport report = new ImportReport();
    report.setId(String.format("%s-%d", prefix, new Date().getTime()));
    report.setStartTime(Calendar.getInstance());

    return report;
  }
  
  public static void stop(ImportReport report) {
    report.setStopTime(Calendar.getInstance());
  }
  
  public static void add(ImportReport report, Exception ex) {

    ImportReportEntry entry = new ImportReportEntry();
    entry.setShortMessage(ex.getMessage());
    
    StringWriter sw = new StringWriter();
    ex.printStackTrace(new PrintWriter(sw));
    
    entry.setLongMessage(sw.toString());
    
    add(report, entry);
  }
  
  public static void add(ImportReport report, String msg) {
    ImportReportEntry entry = new ImportReportEntry();
    
    entry.setShortMessage(msg);

    add(report, entry);
  }
  
  public static void add(ImportReport report, ImportReportEntry entry) {
    entry.setOrder(Long.valueOf(report.getEntry().size()));
    report.getEntry().add(entry);
  }
  
  public static ImportReport combine(ImportReport ... reports) {
    if (reports == null || reports.length == 0) {
      return null;
    }
    if (reports.length == 1) {
      return reports[0];
    }
    
    ImportReport newReport = new ImportReport();
    newReport.setStartTime(reports[0].getStartTime());
    newReport.setStopTime(reports[reports.length-1].getStopTime());
    
    StringBuilder newReportId = new StringBuilder();
    long order = 0;
    for (ImportReport report : reports) {
      if (newReportId.length() > 0) {
        newReportId.append(',');
      }
      newReportId.append(report.getId());
      for (ImportReportEntry entry : report.getEntry()) {
        entry.setOrder(order++);
        newReport.getEntry().add(entry);
      }
    }
    newReport.setId(newReportId.toString());
    return newReport;
  }
}
