package com.example.ecommerce.service;

@Service
class ProductServiceImplementation implements ProductService{

      @Override
      ProductDTO createProduct(ProductDTO productDTO, Long categoryId){
          Category catgeory = categoryRespository.findById(categoryId).orElseThrow(() => new ResourceNotFoundException("category","categoryId",categoryId));

          Product mappedProduct = modelMapper.map(productDTO, Product.class);
          mappedProduct.setSpecialPrice(productDTO.getPrice()-(0.01* productDTO.getPrice() *  productDTO.getDiscount()));
          mappedProduct.setCategoryId(categoryId);

          Product savedProduct = productRepository.save(mappedProduct);

          ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);

          return savedProductDTO;
  
      }
    
} 
