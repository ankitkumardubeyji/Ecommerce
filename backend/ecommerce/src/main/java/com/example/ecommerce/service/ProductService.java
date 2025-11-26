package com.example.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponse;

public interface ProductService{

        ProductDTO createProduct(ProductDTO productDTO, Long categoryId);

        ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder,String categoryName,String keyword);

        /*
        ProductResponse searchProductByCategory(Long categoryId);

        ProductResponse searchProductByKeyword(String keyword);

        ProductDTO deleteProduct(Long productId);

        ProductDTO updateProduct(ProductDTO productDTO , Long  productId);

        ProductDTO updateProductImage(Long productId, MultipartFile image);*/
        
    
}
