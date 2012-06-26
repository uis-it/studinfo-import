package no.uis.service.fsimport.util;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import no.uis.service.studinfo.data.FreeText;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FreeTextAdapter {

  public static String stringValue(FreeText text) throws Exception {
    StringBuilder sb = new StringBuilder();
    
    TransformerFactory transformerFactory = null;
    for(Object item : text.getContent()) {
      if (item instanceof String) {
        sb.append(item.toString());
      } else if (item instanceof Element) {
        if (transformerFactory == null) {
          transformerFactory = TransformerFactory.newInstance();
        }
        DOMSource domSource = new DOMSource((Node)item);
        StringWriter sw = new StringWriter();
        StreamResult streamResult = new StreamResult(sw);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "html");
        transformer.transform(domSource, streamResult);
        sb.append(sw.toString());
      }
    }
    return sb.toString();
  }

}
