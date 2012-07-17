package no.uis.service.studinfo.data;

import java.util.Calendar;

public class FsFagperson {

  private String personId;
  private String fnr;
  private String personrolle;
  private Personnavn personnavn;
  private Calendar datoFra;
  private Calendar datoTil;

  public String getPersonId() {
    return personId;
  }

  public void setPersonId(String personId) {
    this.personId = personId;
  }

  public String getFnr() {
    return fnr;
  }

  public void setFnr(String fnr) {
    this.fnr = fnr;
  }

  public String getPersonrolle() {
    return personrolle;
  }

  public void setPersonrolle(String personrolle) {
    this.personrolle = personrolle;
  }

  public Personnavn getPersonnavn() {
    return personnavn;
  }

  public void setPersonnavn(Personnavn personnavn) {
    this.personnavn = personnavn;
  }

  public Calendar getDatoFra() {
    return datoFra;
  }

  public void setDatoFra(Calendar datoFra) {
    this.datoFra = datoFra;
  }

  public Calendar getDatoTil() {
    return datoTil;
  }

  public void setDatoTil(Calendar datoTil) {
    this.datoTil = datoTil;
  }
}
