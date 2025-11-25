package com.example.ecommerce.service;

interface ProductService{

        ProductDTO createProduct(ProductDTO productDTO, Long categoryId);

        ProductResponse getAllProducts(pageNumber,pageSize,sortBy,sortOrder,categoryName,keyword);
    
}
