package no.uis.service.fsimport.util;

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

public class PropertyInfoUtils {

  private static final Pattern PROP_PATTERN = Pattern.compile("(get|set|isSet|is|has)([A-Z].*)");
  private static final List<String> SKIP_PROPERTIES = Arrays.asList("class", "declaringClass");
  private static final Map<Class<?>, List<PropertyInfo>> cache = new HashMap<Class<?>, List<PropertyInfo>>();

  public static List<PropertyInfo> getPropertyInfos(Class<?> cls) {
    List<PropertyInfo> infos = cache.get(cls);
    if (infos == null) {
      synchronized (cache) {
        infos = cache.get(cls);
        if (infos == null) {
          infos = createPropertyInfos(cls);
          cache.put(cls, infos);
        }
      }
    }
    return infos;
  }

  private PropertyInfoUtils() {
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

        switch (prefix)
          {
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
          }
      }
    }

    List<PropertyInfo> plist = new LinkedList<PropertyInfo>();
    for (String propName : propNames) {
      plist.add(new PropertyInfo(propName, setters.get(propName), getters.get(propName), isSetters.get(propName)));
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
