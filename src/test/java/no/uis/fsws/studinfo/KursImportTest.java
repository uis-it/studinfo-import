/*
 Copyright 2013 University of Stavanger, Norway

 Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package no.uis.fsws.studinfo;

// CHECKSTYLE:OFF
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.Reader;
import java.io.StringReader;

import javax.xml.datatype.XMLGregorianCalendar;

import no.uis.fsws.studinfo.StudInfoImport;
import no.uis.fsws.studinfo.data.FsStudieinfo;
import no.uis.fsws.studinfo.data.Kurs;

import org.junit.Test;

public class KursImportTest {

  @Test
  public void testKursImport() throws Exception {
    StudInfoImport siImport = new EmptyStudinfoImport() {

      @Override
      protected Reader fsGetKurs(int institution, String language) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<fs-studieinfo xmlns=\"http://fsws.usit.no/schemas/studinfo\">");
        sb.append("  <kurs sprak=\"BOKM\u00c5L\">");
        sb.append("    <kursid>");
        sb.append("      <kurskode>DIGIMATTE</kurskode>");
        sb.append("      <tidkode>2012-2013</tidkode>");
        sb.append("    </kursid>");
        sb.append("    <kursnavn>Digitale verkt\u00f8y og unders\u00f8kende arbeidsformer i matematikk</kursnavn>");
        sb.append("    <fagansvarlig>");
        sb.append("      <institusjonsnr>217</institusjonsnr>");
        sb.append("      <fakultetsnr>3</fakultetsnr>");
        sb.append("      <instituttnr>6</instituttnr>");
        sb.append("      <gruppenr>0</gruppenr>");
        sb.append("      <navn>UiS Pluss: Etter- og videreutdanning</navn>");
        sb.append("      <avdnavn>Ledelse og stab</avdnavn>");
        sb.append("    </fagansvarlig>");
        sb.append("    <adminansvarlig>");
        sb.append("      <institusjonsnr>217</institusjonsnr>");
        sb.append("      <fakultetsnr>3</fakultetsnr>");
        sb.append("      <instituttnr>6</instituttnr>");
        sb.append("      <gruppenr>0</gruppenr>");
        sb.append("      <navn>UiS Pluss: Etter- og videreutdanning</navn>");
        sb.append("      <avdnavn>Ledelse og stab</avdnavn>");
        sb.append("    </adminansvarlig>");
        sb.append("    <dato-opptak-fra>2012-03-01</dato-opptak-fra>");
        sb.append("    <dato-opptak-til>2012-06-15</dato-opptak-til>");
        sb.append("    <dato-frist-soknad>2012-06-15</dato-frist-soknad>");
        sb.append("    <email>info@uis.no</email>");
        sb.append("    <fjernundervisning>N</fjernundervisning>");
        sb.append("    <desentral-undervisning>N</desentral-undervisning>");
        sb.append("    <nettbasert-undervisning>N</nettbasert-undervisning>");
        sb.append("    <kan-tilbys>N</kan-tilbys>");
        sb.append("    <skal-avholdes>N</skal-avholdes>");
        sb.append("    <dato-publiseres-fra>2012-03-01</dato-publiseres-fra>");
        sb.append("    <dato-publiseres-til>2012-12-30</dato-publiseres-til>");
        sb.append("    <kurskategori-liste>");
        sb.append("    <kurskategorikode>SKOLE</kurskategorikode>");
        sb.append("    <kurskategorinavn>Skole og barnehage</kurskategorinavn>");
        sb.append("    </kurskategori-liste>");
        sb.append("  </kurs>");
        sb.append("</fs-studieinfo>");

        return new StringReader(sb.toString());
      }
    };
    
    final FsStudieinfo sinfo = siImport.fetchCourses(217, 2013, "HOST", "B");
    
    assertThat(sinfo, is(notNullValue()));
    assertThat(sinfo.getEmne(), is(notNullValue()));
    assertThat(sinfo.getEmne().size(), is(0));
    assertThat(sinfo.getStudieprogram(), is(notNullValue()));
    assertThat(sinfo.getStudieprogram().size(), is(0));
    assertThat(sinfo.getKurs(), is(notNullValue()));
    assertThat(sinfo.getKurs().size(), is(1));
    Kurs kurs = sinfo.getKurs().get(0);
    assertThat(kurs.getKursid().getKurskode(), is("DIGIMATTE"));
    // TODO should the date be of type java.util.Calendar?
    assertThat(kurs.getDatoOpptakFra(), is(instanceOf(XMLGregorianCalendar.class)));
    assertThat(kurs.getDatoOpptakFra().toXMLFormat(), is("2012-03-01"));
    assertThat(kurs.getAdminansvarlig(), is(notNullValue()));
    assertThat(kurs.getFagansvarlig(), is(notNullValue()));
  }
}
