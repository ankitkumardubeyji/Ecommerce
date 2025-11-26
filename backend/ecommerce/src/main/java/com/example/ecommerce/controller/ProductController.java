package com.example.ecommerce.controller;

import java.io.IOException;

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
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.config.AppConstants;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponse;
import com.example.ecommerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController{
	
	@Autowired
	private ProductService productService;

	@PostMapping("admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId){
    	ProductDTO savedProductDTO = productService.createProduct(productDTO,categoryId);
		return new ResponseEntity<>(savedProductDTO,HttpStatus.CREATED);
    }

	@GetMapping("/public/products")							
	public ResponseEntity<ProductResponse> getAllProducts(
			@RequestParam(name ="pageNumber", defaultValue = AppConstants.pageNumber, required=false)Integer pageNumber,
			@RequestParam(name="pageSize",defaultValue =  AppConstants.pageSize,required=false)Integer pageSize,
			@RequestParam(name="sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
			@RequestParam(name="sortOrder",defaultValue =  AppConstants.SORT_DIR , required=false) String sortOrder,
			@RequestParam(name="category",required=false) String category,
			@RequestParam(name="keyword" , required = false) String keyword){

			ProductResponse productResponse = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder,category,keyword);
			return new ResponseEntity<>(productResponse,HttpStatus.OK);
		
	}

	/*
	@GetMapping("public/products/{categoryId}")
	public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId){
		ProductResponse productResponse = productService.searchProductByCategory(categoryId);
		return new ResponseEntity<>(productResponse,HttpStatus.OK);
	}

	@GetMapping("/public/products/{keyword}")								
	public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword){
		ProductResponse productResponse = productService.searchProductByKeyword(keyword);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@DeleteMapping("/admin/products/{productId}")
	public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
		ProductDTO deletedProductDTO = productService.deleteProduct(productId);
		return new ResponseEntity<>(deletedProductDTO, HttpStatus.OK);
		}	

	@PutMapping("/admin/products/{productId}")						
	public ResponseEntity<ProductDTO> updateProduct(ProductDTO productDTO, @PathVariable Long productId){
		 ProductDTO updatedProductDTO = productService.updateProduct(productDTO,productId);
		 return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
		}
		

	@PutMapping("/products/{productId}/image")								
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException{
		ProductDTO updatedProductDTO = productService.updateProductImage(productId,image);
		return new ResponseEntity<>(updatedProductDTO,HttpStatus.OK);
	}
									
*/
									
}
