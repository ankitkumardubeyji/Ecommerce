package com.example.ecommerce.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.exceptions.ApiException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.repositories.CategoryRepository;

@Service
public class CategoryServiceImplementation implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		// TODO Auto-generated method stub
		
		Category category = modelMapper.map(categoryDTO,Category.class);
		
		Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
		if(existingCategory!=null) {
			throw new ApiException(category.getCategoryName()+" already exists in the database");
		}
		
		Category savedCategory = categoryRepository.save(category);
		
		CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
		
		return savedCategoryDTO;
		
	}

}
