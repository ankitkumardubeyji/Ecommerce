package com.example.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ecommerce.exceptions.ApiException;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.payload.CategoryDTO;
import com.example.ecommerce.payload.CategoryResponse;
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


	@Override
	public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		// TODO Auto-generated method stub
		
		
		// Defining the sorting strategy : SORT object
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				 ?Sort.by(sortBy).ascending()
				 :Sort.by(sortBy).descending();
		
		
		
		// Create a pageable request object :with pageNumber,pageSize,sortByAndOrder
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortByAndOrder);
		
		// Executes the query with the given pagination and sorting order
		Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
		
		// Extracting the categories from the Page<Category> wrapper:-
		List<Category> categories = categoryPage.getContent();
		
		if(categories.isEmpty()) {
			throw new ApiException("No Category Present sorry!");
		}
		
		
		// Mapping all the category objects to CategoryDTO objects:-
		
		List<CategoryDTO> categoryDTO = categories.stream()
								.map(category -> modelMapper.map(category, CategoryDTO.class))
								.collect(Collectors.toList());
		
		
		CategoryResponse categoryResponse = new CategoryResponse();
		
		categoryResponse.setContent(categoryDTO);
		categoryResponse.setPageNumber(categoryPage.getNumber());
		categoryResponse.setTotalElements(categoryPage.getTotalElements());;
		categoryResponse.setTotalPages(categoryPage.getTotalPages());
		categoryResponse.setPageSize(categoryPage.getSize());
		categoryResponse.setLastPage(categoryPage.isLast());
		
	
		return categoryResponse;
	}


	@Override
	public CategoryDTO deleteCategory(Long categoryId) {
		// TODO Auto-generated method stub
		
		Category categoryToBeDeleted = categoryRepository.findById(categoryId)
										.orElseThrow( () -> new ResourceNotFoundException("Category","categoryId",categoryId));
		
		categoryRepository.delete(categoryToBeDeleted);
		
		
		CategoryDTO deletedCategoryDTO = modelMapper.map(categoryToBeDeleted, CategoryDTO.class);
		return deletedCategoryDTO;
	}


	@Override
	public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
		// TODO Auto-generated method stub
		
		// Checking if categoryTobeUpdated exists or not
		Category categoryToBeUpdated = categoryRepository.findById(categoryId)
										.orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
		
		
		categoryToBeUpdated.setCategoryId(categoryId);
		System.out.println("**********");
		System.out.println(categoryDTO.getCategoryName());
		System.out.println("**********");
		
		
		categoryToBeUpdated.setCategoryName(categoryDTO.getCategoryName());
		
		
		// Saving the updated category in the database
		Category categoryUpdated = categoryRepository.save(categoryToBeUpdated);
		
		return modelMapper.map(categoryUpdated,CategoryDTO.class);
	}
	
	


}
