package br.com.growe.growe_backend.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {

  private final String resourceType;
  private final String resourceId;

  public ResourceNotFoundException(String resourceType, String resourceId) {
    super(
        String.format("%s not found with id: %s", resourceType, resourceId),
        HttpStatus.NOT_FOUND,
        "RESOURCE_NOT_FOUND"
    );
    this.resourceType = resourceType;
    this.resourceId = resourceId;
  }

  public String getResourceType() {
    return resourceType;
  }

  public String getResourceId() {
    return resourceId;
  }
}