package com.example.ecommerce.exceptions;

public class ResourceNotFoundException  extends RuntimeException{

	String resourceName;
	String field;
	Long fieldId;
	
	public ResourceNotFoundException() {
		
	}
	
	
	public ResourceNotFoundException(String resourceName , String field,Long fieldId) {
		super(String.format("%s not found with %s:%d",resourceName,field,fieldId));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldId = fieldId;
	}
	
}
