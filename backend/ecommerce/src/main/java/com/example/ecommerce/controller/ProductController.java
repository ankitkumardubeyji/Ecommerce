package com.example.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
class ProductController{

	@PostMapping("admin/categories/{categoryId}/product)
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable categoryId){
    	ProductDTO savedProductDTO = productService.createProduct(productDTO,categoryId);
		return new ResponseEntity<>(savedProductDTO,HttpStatus.CREATED);
    }

	public ResponseEntity<CategoryResponse> getAllCategories(
			@RequestParam(name="pageNumber",defaultValue = AppConstants.pageNumber,required = false)Integer pageNumber,
			@RequestParam(name="pageSize",defaultValue = AppConstants.pageSize, required = false) Integer pageSize,
			@RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY , required = false)String sortBy,
			@RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
		
		CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
		return new ResponseEntity<CategoryResponse>(categoryResponse,HttpStatus.OK);

	}

	public ResponseEntity<ProductResponse> getAllProducts(
			@RequestParam(name ="pageNumber", defaultValue = AppConstants.pageNumber, required=false)Integer pageNumber,
			@RequestParam(name="pageSize",defaultValye =  AppConstants.pageSize,required=false)Integer pageSize,
			@RequestParam(name="sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
			@RequestParam(name="sortOrder",defaultValue =  AppConstants.SORT_DIR , required=false) String sortOrder,
			@RequestParam(name="categoryName",defaultValue = AppConstants.CATEGORY_NAME, required=false) String categoryName,
			@RequestParam(name="keyword , defaultValue = AppConstants.KEYWORD, required = false) String keyword){
		}

									
									

}
