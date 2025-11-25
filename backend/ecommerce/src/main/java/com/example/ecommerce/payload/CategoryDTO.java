package com.example.ecommerce.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO {

	private Long categoryId;

	//@NotBlank
	//@Size(min=5, message="Category name must be having atleast 5 characters")
	private String categoryName;
	
	public CategoryDTO() {
		
	}

	public CategoryDTO(Long categoryId, String categoryName) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	
	
}
