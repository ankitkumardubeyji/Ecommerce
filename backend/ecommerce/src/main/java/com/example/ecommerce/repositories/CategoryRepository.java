package com.example.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{

}
