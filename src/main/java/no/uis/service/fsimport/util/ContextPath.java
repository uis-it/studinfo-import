package no.uis.service.fsimport.util;

import java.util.Iterator;
import java.util.Stack;

public class ContextPath extends ThreadLocal<Stack<String>> implements Iterable<String> {

  @Override
  protected Stack<String> initialValue() {
    return createStack("/");
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
    s.add("/" + elem);
    return s;
  }
  
  private static class PathStack extends Stack<String> {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (String elem : this) {
        sb.append(elem);
        sb.append('/');
      }
      return sb.toString();
    }
    
    
  }

  @Override
  public Iterator<String> iterator() {
    return super.get().iterator();
  }
}
