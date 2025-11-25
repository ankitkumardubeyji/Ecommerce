package com.example.ecommerce.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId; // categoryId :will be the primary key with autoincrement feature
	
	@NotBlank
	@Size(min=5,message = "Minimum 5 should be the size for the categoryName")
	private String categoryName;

	@OneToMany(mappedBy="category") // No need to create product column in the Category table it will be handled by the Child table "category" field corresponding column
	private List<Product> product;
	
		
}
