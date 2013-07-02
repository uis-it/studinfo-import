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

import java.io.Reader;

import no.uis.fsws.studinfo.impl.AbstractStudinfoImport;

/**
 * Dummy implementation for StudinfoImport. 
 */
public class EmptyStudinfoImport extends AbstractStudinfoImport {

  @Override
  protected Reader fsGetKurs(int institution, String language) {
    return null;
  }

  @Override
  protected Reader fsGetEmne(int institution, int faculty, int year, String semester, String language) {
    return null;
  }

  @Override
  protected Reader fsGetStudieprogram(int institution, int faculty, int year, String semester, boolean includeEP, String language)
  {
    return null;
  }

  @Override
  protected Reader fsGetEmne(int institution, String emnekode, String versjonskode, int year, String semester, String language) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Reader fsGetStudieprogram(String studieprogramkode, int year, String semester, boolean includeEP, String language) {
    // TODO Auto-generated method stub
    return null;
  }

}
