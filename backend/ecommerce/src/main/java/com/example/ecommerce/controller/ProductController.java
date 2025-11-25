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

	@GetMapping("/public/products")							
	public ResponseEntity<ProductResponse> getAllProducts(
			@RequestParam(name ="pageNumber", defaultValue = AppConstants.pageNumber, required=false)Integer pageNumber,
			@RequestParam(name="pageSize",defaultValye =  AppConstants.pageSize,required=false)Integer pageSize,
			@RequestParam(name="sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
			@RequestParam(name="sortOrder",defaultValue =  AppConstants.SORT_DIR , required=false) String sortOrder,
			@RequestParam(name="categoryName",defaultValue = AppConstants.CATEGORY_NAME, required=false) String categoryName,
			@RequestParam(name="keyword , defaultValue = AppConstants.KEYWORD, required = false) String keyword){

			ProductResponse productResponse = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder,categoryName,keyword);
			return new ResponseEntity<>(productResponse,HttpStatus.OK);
		
	}
}
