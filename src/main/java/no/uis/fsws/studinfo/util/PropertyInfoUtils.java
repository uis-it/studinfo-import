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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for getting property information form an object.
 */
public class PropertyInfoUtils {

  private static final Pattern PROP_PATTERN = Pattern.compile("(get|set|isSet|is|has)([A-Z].*)");
  private static final List<String> SKIP_PROPERTIES = Arrays.asList("class", "declaringClass");
  private static final Map<Class<?>, List<PropertyInfo>> CACHE = new HashMap<Class<?>, List<PropertyInfo>>();

  private PropertyInfoUtils() {
  }
  
  public static List<PropertyInfo> getPropertyInfos(Class<?> cls) {
    List<PropertyInfo> infos = CACHE.get(cls);
    if (infos == null) {
      synchronized (CACHE) {
        infos = CACHE.get(cls);
        if (infos == null) {
          infos = createPropertyInfos(cls);
          CACHE.put(cls, infos);
        }
      }
    }
    return infos;
  }

  private static List<PropertyInfo> createPropertyInfos(Class<?> beanClass) {
    Set<String> propNames = new HashSet<String>();
    Map<String, Method> getters = new HashMap<String, Method>();
    Map<String, Method> setters = new HashMap<String, Method>();
    Map<String, Method> isSetters = new HashMap<String, Method>();
    for (Method m : beanClass.getMethods()) {
      String mname = m.getName();
      Matcher propMatcher = PROP_PATTERN.matcher(mname);
      if (propMatcher.matches()) {

        String propName = toPropertyName(propMatcher.group(2));
        
        if (SKIP_PROPERTIES.contains(propName)) {
          continue;
        }
        String prefix = propMatcher.group(1);
        propNames.add(propName);

        switch (prefix) {
            case "get":
            case "is":
            case "has":
              getters.put(propName, m);
              break;
            case "set":
              setters.put(propName, m);
              break;
            case "isSet":
              isSetters.put(propName, m);
              break;
            default:
              break;
        }
      }
    }

    List<PropertyInfo> plist = new LinkedList<PropertyInfo>();
    for (String propName : propNames) {
      plist.add(new PropertyInfo(propName, getters.get(propName), setters.get(propName), isSetters.get(propName)));
    }
    return plist;
  }

  private static String toPropertyName(String orig) {
    StringBuilder sb = new StringBuilder();
    sb.append(orig.substring(0, 1).toLowerCase());
    sb.append(orig, 1, orig.length());
    return sb.toString();
  }

}
