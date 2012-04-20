package no.uis.service.fsimport.impl;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Namespace handler that handles missing namespace from database XML.
 * taken from http://stackoverflow.com/questions/277502/jaxb-how-to-ignore-namespace-during-unmarshalling-xml-document
 */
public class StudinfoNamespaceFilter extends XMLFilterImpl {

  private String usedNamespaceUri;
  private boolean addNamespace;

  //State variable
  private boolean addedNamespace = false;

  public StudinfoNamespaceFilter(String namespaceUri,
          boolean addNamespace) {
      super();

      if (addNamespace) {
          this.usedNamespaceUri = namespaceUri;
      } else { 
          this.usedNamespaceUri = "";
      }
      this.addNamespace = addNamespace;
  }



  @Override
  public void startDocument() throws SAXException {
      super.startDocument();
      if (addNamespace) {
          startControlledPrefixMapping();
      }
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    super.startElement(this.usedNamespaceUri, localName, qName, atts);
  }

  @Override
  public void endElement(String arg0, String arg1, String arg2)
          throws SAXException {

    super.endElement(this.usedNamespaceUri, arg1, arg2);
  }

  @Override
  public void startPrefixMapping(String prefix, String url)
          throws SAXException {

      if (addNamespace) {
          this.startControlledPrefixMapping();
      } else {
          //Remove the namespace, i.e. don´t call startPrefixMapping for parent!
      }

  }

  private void startControlledPrefixMapping() throws SAXException {

      if (this.addNamespace && !this.addedNamespace) {
          //We should add namespace since it is set and has not yet been done.
          super.startPrefixMapping("", this.usedNamespaceUri);

          //Make sure we dont do it twice
          this.addedNamespace = true;
      }
  }

}
