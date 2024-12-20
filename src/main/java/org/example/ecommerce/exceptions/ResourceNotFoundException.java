package org.example.ecommerce.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    private String field;
    private String fieldName;
    private Long fieldId;
    private String resourceName;

    public ResourceNotFoundException(){

    }

    public ResourceNotFoundException(String resourceName,String field, String fieldName) {
        super(String.format(("%s not found with %s:%s"), resourceName, field, fieldName));
        this.field = field;
        this.fieldName = fieldName;
        this.resourceName = resourceName;
    }

    public ResourceNotFoundException(String resourceName,String field,Long fieldId) {
        super(String.format(("%s not found with %s:%s"), resourceName, field, fieldId));
        this.field = field;
        this.fieldId = fieldId;
        this.resourceName = resourceName;
    }
}
