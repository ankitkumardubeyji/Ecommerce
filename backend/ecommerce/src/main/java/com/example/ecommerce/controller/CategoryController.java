package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.config.AppConstants;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponse;
import com.example.ecommerce.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api") // we want all the controllers to be accepting request from a specific end point
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/public/categories")
	public ResponseEntity<CategoryDTO> createCategory(
	        @Valid @RequestBody CategoryDTO categoryDTO) {
	    CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
	    return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
	  
	}

	
	@GetMapping("public/categories")
	public ResponseEntity<CategoryResponse> getAllCategories(
			@RequestParam(name="pageNumber",defaultValue = AppConstants.pageNumber,required = false)Integer pageNumber,
			@RequestParam(name="pageSize",defaultValue = AppConstants.pageSize, required = false) Integer pageSize,
			@RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY , required = false)String sortBy,
			@RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
		
		CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
		return new ResponseEntity<CategoryResponse>(categoryResponse,HttpStatus.OK);
		
		
		
	}
	
	@DeleteMapping("admin/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
		CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(deletedCategoryDTO,HttpStatus.OK);
	}
	
	@PutMapping("/public/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO){
		CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryId,categoryDTO);
		return new ResponseEntity<>(updatedCategoryDTO,HttpStatus.OK);
	}
	
}
