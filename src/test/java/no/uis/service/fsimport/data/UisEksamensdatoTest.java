package no.uis.service.fsimport.data;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import no.uis.service.studinfo.data.FsEksamensdato;

import org.junit.Test;

public class UisEksamensdatoTest {

  private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy"); 
  
  @Test
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
  
  @Test
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
  
  @Test
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
  
  @Test
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
