package no.uis.service.fsimport.mock;



public class ResourceAnswer extends AbstractResourceAnswer  {

  private String resourceName;

  public ResourceAnswer(String resourceName) {
    this.resourceName = resourceName;
  }

  protected String getResourceName() {
    return resourceName;
  }
}