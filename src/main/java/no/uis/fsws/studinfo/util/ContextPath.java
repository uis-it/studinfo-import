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

package no.uis.fsws.studinfo.util;

import java.util.Iterator;
import java.util.Stack;

/**
 * Keeps track of the hierarchy of the parsed document.
 */
public class ContextPath extends ThreadLocal<Stack<String>> implements Iterable<String> {

  private static final String SLASH = "/";

  @Override
  protected Stack<String> initialValue() {
    return createStack(SLASH);
  }

  public void init(String initElement) {
    super.set(createStack(initElement));
  }
  
  public void push(String elem) {
    super.get().push(elem);
  }
  
  public String pop() {
    return super.get().pop();
  }
  
  public String peek() {
    return super.get().peek();
  }
  
  public String getPath() {
    return super.get().toString();
  }
  
  
  private Stack<String> createStack(String elem) {
    Stack<String> s = new PathStack();
    s.add(SLASH + elem);
    return s;
  }
  
  private static class PathStack extends Stack<String> {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (String elem : this) {
        sb.append(elem);
        sb.append(SLASH);
      }
      return sb.toString();
    }
  }

  @Override
  public Iterator<String> iterator() {
    return super.get().iterator();
  }
}
