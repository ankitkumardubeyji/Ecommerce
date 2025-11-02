package com.example.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.ecommerce.payload.ApiResponse;

@RestControllerAdvice // makes method inside this as the global exception handler , throughout the application the exception defined below will be handled  
public class MyGlobalExceptionHandler {

	
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse> myApiException(ApiException e){
		String message = e.getMessage();
		ApiResponse apiResponse = new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
	}
	
	
}
