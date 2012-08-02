package no.uis.service.fsimport;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class ValidatorTest {

  /**
   * The test is disabled by default. It can be enabled by setting the system properties {@code studinfo.year} and {@code studinfo.semester}. The year
   * is set with the system property {@code studinfo-validate.year}. The semester is set with the system property {@code studinfo-validate.semester}.
   */
  @Test
  public void validateAll() throws Exception {
    final String year = System.getProperty("studinfo.year");
    final String semester = System.getProperty("studinfo.semester");
    if (year != null && semester != null) {
      StudinfoValidator sinfo = new StudinfoValidator();
      final List<String> messages = sinfo.validateAll(year, semester);
      assertThat(new ListWrapper(messages), is(emptyList()));
    } else {
      assertTrue(true);
    }
  }

  private static class IsEmptyCollection extends BaseMatcher<Collection<?>> {

    @Override
    public boolean matches(Object item) {
      return (item != null && ((Collection<?>)item).isEmpty());
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("empty");
    }
  }

  private static Matcher<Collection<?>> emptyList() {
    return new IsEmptyCollection();
  }

  /**
  * This class overrides the {@link AbstractCollection#toString()} method so that the matcher puts every message on a line.
  */
  @SuppressWarnings("serial")
  private static class ListWrapper extends ArrayList<String> {
    
    public ListWrapper(List<String> wrappee) {
      super(wrappee);
    }
    
    @Override
    public String toString() {
      Iterator<?> i = iterator();
      if (!i.hasNext()) {
        return "[]";
      }

      StringBuilder sb = new StringBuilder();
      sb.append("[\n");
      for (;;) {
        Object e = i.next();
        sb.append(e == this ? "(this Collection)" : e);
        if (!i.hasNext()) {
          return sb.append("\n]").toString();
        }
        sb.append(", \n");
      }
    }
    
  }
}
