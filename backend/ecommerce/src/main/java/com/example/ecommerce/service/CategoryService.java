package com.example.ecommerce.service;

import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponse;

public interface CategoryService {

	CategoryDTO createCategory(CategoryDTO categoryDTO);

	CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	CategoryDTO deleteCategory(Long categoryId);

	CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);

}
