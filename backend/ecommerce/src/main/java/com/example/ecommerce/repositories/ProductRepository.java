package com.example.ecommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product>{
		List<Product> findByCategoryOrderByPriceAsc(Category category);
		
		List<Product> findByProductNameLikeIgnoreCase(String keyword);
}

