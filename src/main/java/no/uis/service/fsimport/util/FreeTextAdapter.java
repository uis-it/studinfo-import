package no.uis.service.fsimport.util;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import no.uis.service.studinfo.data.FreeText;

import org.w3c.dom.Element;

public class FreeTextAdapter {

  public static String stringValue(FreeText text) throws Exception {
    return convertFreeText(text, new ElementToHtmlConverter());
  }

  public static String plainStringValue(FreeText text) throws Exception {
    return convertFreeText(text, new ElementToPlainConverter());
  }

  private static String convertFreeText(FreeText text, ElementConverter converter) throws Exception {
    
    if (text == null || !text.isSetContent()) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    
    for(Object item : text.getContent()) {
      if (item instanceof String) {
        sb.append(item.toString());
      } else if (item instanceof Element) {
        sb.append(converter.stringValue((Element)item));
      }
    }
    return sb.toString();
  }
  
  private static class ElementToHtmlConverter implements ElementConverter {

    private TransformerFactory factory = TransformerFactory.newInstance();
    
    @Override
    public String stringValue(Element element) throws Exception {
      DOMSource domSource = new DOMSource(element);
      StringWriter sw = new StringWriter();
      StreamResult streamResult = new StreamResult(sw);
      Transformer transformer = factory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.METHOD, "html");
      transformer.transform(domSource, streamResult);
      return sw.toString();
    }
    
  }
  
  public static class ElementToPlainConverter implements ElementConverter {

    @Override
    public String stringValue(Element element) throws Exception {
      return element.getTextContent();
    }
  }

  private interface ElementConverter {
    
    String stringValue(Element element) throws Exception;
  }
}
