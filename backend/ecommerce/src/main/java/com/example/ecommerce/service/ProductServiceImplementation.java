package com.example.ecommerce.service;

@Service
class ProductServiceImplementation implements ProductService{

      @Value("${image.base.url}") // fetching the base url for the image from the application.properties 
	private String imageBaseUrl;

      @Override
      ProductDTO createProduct(ProductDTO productDTO, Long categoryId){
          Category catgeory = categoryRespository.findById(categoryId).orElseThrow(() => new ResourceNotFoundException("category","categoryId",categoryId));


         // Checking if product with the same name if already exists in the database
         boolean isProductPresentAlready = false;

         List<Product> products = category.getProducts(); // Getting the products that are present in the given category 

         for(Product product: products){
               if(product.getProductName().equals(productDTO.getProductName)){
                        throw new ApiException("Product with the given name already exists in the database");
               }
         }
            
            
      
          Product mappedProduct = modelMapper.map(productDTO, Product.class);
          mappedProduct.setSpecialPrice(productDTO.getPrice()-(0.01* productDTO.getPrice() *  productDTO.getDiscount()));
          mappedProduct.setCategoryId(categoryId);

          Product savedProduct = productRepository.save(mappedProduct);

          ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);

          return savedProductDTO;
  
      }

      @Override
      ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder,String categoryName,String keyword){
            Sort SortByAndOrder = sortOrder.equalsIgnoreCase("asc")            
                                          ?Sort.by(sortBy).ascending()
                                          :Sort.by(sortBy).descending();
            Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);

            Specification<Product> spec = Specification.where(null) ; // Preparing the filter recepie starting with the empty filter

            if(keyword!=null && !keyword.isEmpty()){
                        spec.and((root,query,criteriaBuilder)=>{
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + keyword.toLowerCase()+"%");
                                );
            }

            if(category!=null && !category.isEmpty()){
                          spec.and((root,query,createBuilder)=>{
                                  criteriaBuilder.like(root.get("category").get("categoryName"),categoryName)
                          }
                                  );
            }

            Page<Product> productPage = pageRepository.findAll(pageDetails,spec); // executing the query with the given pagination and sorting

            // Getting the list of products from the productPage
            List<Product> products = productPage.getContent();

            if(product.isEmpty()){
                  throw new ApiException("No product exists");
            }

            // Mapping from product to productDTO
            ProductDTO fetchedProductDTO = products.map((product) -> {
                 ProductDTO productDTO =  modelMapper.map(product,ProductDTO.class)
                 productDTO.setImage(constructImageUrl(product.getImage())); // Construction of the image is pending here 
                 return productDTO;
                        });

            ProductResponse productResponse = new ProductResponse();
            productResponse.setContent(fetchedProductDTO);
            productResponse.setPageNumber(productPage.getPageNumber());
            productResponse.setPageSize(productPage.getPageSize());
            productResponse.setTotalElements(product.getTotalElements());
            productResponse.setTotalPages(product.getTotalPages());
            productResponse.setisLast(product.isLast());
            return productResponse;      
      }

      public String constructImageUrl(String imageName){
            return imageName.endsWith("/")?imageBaseUrl+ imageName : imageBaseUrl + "/" + imageName);
                  "
      }
    
} 
