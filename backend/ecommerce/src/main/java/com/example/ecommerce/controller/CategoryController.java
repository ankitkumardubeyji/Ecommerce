package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.service.CategoryService;

@RestController
@RequestMapping("/api") // we want all the controllers to be accepting request from a specific end point
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/public/categories")
	public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO){
		CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
		return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
	}
	
	
	
}
