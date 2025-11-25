package com.example.ecommerce.service;

interface ProductService{

        ProductDTO createProduct(ProductDTO productDTO, Long categoryId);

        ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder,String categoryName,String keyword);

        ProductResponse searchProductByCategory(Integer categoryId);

        ProductResponse searchProductByKeyword(String keyword);

        ProductResponse deleteProduct(Integer productId);

        ProductResponse updateProduct(ProductDTO productDTO , Integer  productId);

        ProductResponse updateProductImage(Integer productId, MultipartFile image);
        
    
}
