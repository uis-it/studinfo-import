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

package no.uis.fsws.studinfo.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Properties;

import lombok.extern.log4j.Log4j;

import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLEntityScanner;

/**
 * Facilitates parsing of dirty XML. 
 * @see http://chickensilver.blogspot.no/2013/02/how-to-parse-dirty-xml.html
 */
@Log4j
public class EntityManager extends XMLEntityManager {
  
  public EntityManager() {
    addInternalEntities();
  }
  
  private void addInternalEntities() {

    try (InputStream entitiesStream = EntityManager.class.getResourceAsStream("/entities.properties")) {
      if (entitiesStream == null) {
        return;
      }
      Properties entities = new Properties();
      entities.load(entitiesStream);
      for (Entry<Object, Object> entity : entities.entrySet()) {
        addDeclaredInternalEntity(entity.getKey().toString(), entity.getValue().toString());
      }
    } catch(IOException e) {
      log.info("addInternalEntities", e);
    }
  }

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
    boolean declaredEntity = super.isDeclaredEntity(entityName);
    if (!declaredEntity) {
      String entityValue = "&#38;"+entityName+";";
      addInternalEntity(entityName, entityValue);
      addDeclaredInternalEntity(entityName, entityValue);
      declaredEntity = true;
      log.warn("entity \""+entityName+"\" not defined");
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
        return "nbsp";
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
