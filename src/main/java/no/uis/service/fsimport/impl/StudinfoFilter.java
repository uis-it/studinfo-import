package no.uis.service.fsimport.impl;

public interface StudinfoFilter<T> {
  boolean accept(T elem);
}
