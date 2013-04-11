/*
 Copyright 2010-2013 University of Stavanger, Norway

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
// CHECKSTYLE:OFF
package no.uis.service.component.fsimport.data;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import no.uis.service.studinfo.data.FsEksamensdato;

public class UisEksamensdatoTest {

  private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy"); 
  
  //@Test
  public void checkSimpleData() throws Exception {
    FsEksamensdato dato = FsEksamensdato.valueOf("01.08.2012");
    assertThat(dato, is(notNullValue()));
    assertThat(dato.toString(), is("01.08.2012"));

    assertThat(dato.getDato(), is(notNullValue()));
    assertThat(dato.getInnleveringDato(), is(nullValue()));
    assertThat(dato.getInnleveringTid(), is(nullValue()));
    assertThat(dato.getUttak(), is(nullValue()));
    
    String dString = dateFormat.format(dato.getDato().getTime());
    assertThat(dString, is("01.08.2012"));
  }
  
  //@Test
  public void checkUttak() throws Exception {
    FsEksamensdato dato = FsEksamensdato.valueOf("Uttak: 11.05.2012 Frist innlevering: 15.05.2012 kl. 14:00");
    assertThat(dato, is(notNullValue()));
    assertThat(dato.toString(), is("Uttak: 11.05.2012 Frist innlevering: 15.05.2012 kl. 14:00"));
    
    assertThat(dato.getDato(), is(nullValue()));
    assertThat(dato.getUttak(), is(notNullValue()));
    assertThat(dato.getInnleveringDato(), is(notNullValue()));
    assertThat(dato.getInnleveringTid(), is(notNullValue()));
    
    String uttak = dateFormat.format(dato.getUttak().getTime());
    assertThat(uttak, is("11.05.2012"));
    
    String innlevering = dateFormat.format(dato.getInnleveringDato().getTime());
    assertThat(innlevering, is("15.05.2012"));

    assertThat(dato.getInnleveringTid().getHours(), is(14));
    assertThat(dato.getInnleveringTid().getMinutes(), is(0));
  }
  
  //@Test
  public void checkInnleveringDate() throws Exception {
    FsEksamensdato dato = FsEksamensdato.valueOf("Frist innlevering: 15.05.2012");
    assertThat(dato, is(notNullValue()));
    assertThat(dato.toString(), is("Frist innlevering: 15.05.2012"));

    assertThat(dato.getDato(), is(nullValue()));
    assertThat(dato.getUttak(), is(nullValue()));
    assertThat(dato.getInnleveringDato(), is(notNullValue()));
    assertThat(dato.getInnleveringTid(), is(nullValue()));
    
    String innlevering = dateFormat.format(dato.getInnleveringDato().getTime());
    assertThat(innlevering, is("15.05.2012"));
  }
  
  //@Test
  public void checkInnleveringDateTime() throws Exception {
    FsEksamensdato dato = FsEksamensdato.valueOf("Frist innlevering: 15.05.2012 kl. 14:00");
    assertThat(dato, is(notNullValue()));
    assertThat(dato.toString(), is("Frist innlevering: 15.05.2012 kl. 14:00"));

    assertThat(dato.getDato(), is(nullValue()));
    assertThat(dato.getUttak(), is(nullValue()));
    assertThat(dato.getInnleveringDato(), is(notNullValue()));
    assertThat(dato.getInnleveringTid(), is(notNullValue()));
    
    String innlevering = dateFormat.format(dato.getInnleveringDato().getTime());
    assertThat(innlevering, is("15.05.2012"));

    assertThat(dato.getInnleveringTid().getHours(), is(14));
    assertThat(dato.getInnleveringTid().getMinutes(), is(0));
  }
}
