package no.uis.service.fsimport.impl;

import java.io.IOException;
import java.util.Hashtable;

import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLEntityScanner;

public class EntityManager extends XMLEntityManager {
  
  public static final String EMPTY_ENTITY = "XX";

  @Override
  public void setScannerVersion(short version) {
    if (version == Constants.XML_VERSION_1_0) {
      if (fXML10EntityScanner == null) {
        fXML10EntityScanner = new EntityScanner();
      }
      fXML10EntityScanner.reset(fSymbolTable, this, fErrorReporter);
      fEntityScanner = fXML10EntityScanner;
      fEntityScanner.setCurrentEntity(fCurrentEntity);
    } else {
      if (fXML11EntityScanner == null) {
        fXML11EntityScanner = new EntityScanner();
      }
      fXML11EntityScanner.reset(fSymbolTable, this, fErrorReporter);
      fEntityScanner = fXML11EntityScanner;
      fEntityScanner.setCurrentEntity(fCurrentEntity);
    }
  } 
  
  @Override
  public boolean isDeclaredEntity(String entityName) {
    if (entityName == EMPTY_ENTITY) {
      return true;
    }
    
    boolean declaredEntity = super.isDeclaredEntity(entityName);
    if (!declaredEntity) {
      //String entityValue = "&amp;"+entityName+";";
      String entityValue = "&#38;"+entityName+";";
      addInternalEntity(entityName, entityValue);
      addDeclaredInternalEntity(entityName, entityValue);
      declaredEntity = true;
    }
    return declaredEntity;
  }
  
  @SuppressWarnings("unchecked")
  private void addDeclaredInternalEntity(String name, String text) {
    if (fDeclaredEntities == null) {
      fDeclaredEntities = new Hashtable<Object, Object>();
    }
    if (!fDeclaredEntities.containsKey(name)) {
      Entity entity = new InternalEntity(name, text, fInExternalSubset);
      fDeclaredEntities.put(name, entity);
    }
  }

  @Override
  public XMLEntityScanner getEntityScanner() {
    if (fEntityScanner == null) {
      // default to 1.0
      if (fXML10EntityScanner == null) {
        fXML10EntityScanner = new EntityScanner();
      }
      fXML10EntityScanner.reset(fSymbolTable, this, fErrorReporter);
      fEntityScanner = fXML10EntityScanner;
    }
    return fEntityScanner;
  } 

  private static class EntityScanner extends org.apache.xerces.impl.XMLEntityScanner {

    private ScannedEntity oldEntity;

    @Override
    public String scanName() throws IOException {
      String name = super.scanName();
      if (name == null) {
        this.oldEntity = fCurrentEntity;
        fCurrentEntity = null;
        return EMPTY_ENTITY;
      }
      return name;
    }

    @Override
    public boolean skipChar(int c) throws IOException {
      if (fCurrentEntity == null) {
        fCurrentEntity = oldEntity;
        return true;
      }
      return super.skipChar(c);
    }
  }
}
