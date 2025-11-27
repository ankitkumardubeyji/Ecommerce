package com.example.ecommerce.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.exceptions.ApiException;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponse;
import com.example.ecommerce.repositories.CategoryRepository;
import com.example.ecommerce.repositories.ProductRepository;


@Service
public class ProductServiceImplementation implements ProductService{

    @Value("${image.base.url}") // fetching the base url for the image from the application.properties 
	private String imageBaseUrl;

	@Value("${project.image}") 
	private String path;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	
	@Autowired
	private FileService fileService;
	
      @Override
    public ProductDTO createProduct(ProductDTO productDTO, Long categoryId){
          Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category","categoryId",categoryId));


         // Checking if product with the same name if already exists in the database
       

         List<Product> products = category.getProducts(); // Getting the products that are present in the given category 

         for(Product product: products){
               if(product.getProductName().equals(productDTO.getProductName())){
                        throw new ApiException("Product with the given name already exists in the database");
               }
         }
            
            
      
          Product mappedProduct = modelMapper.map(productDTO, Product.class);
          
          mappedProduct.setSpecialPrice(productDTO.getPrice()-(0.01* productDTO.getPrice() *  productDTO.getDiscount()));
          mappedProduct.setCategory(category);
          mappedProduct.setImage("default.png");
          
          Product savedProduct = productRepository.save(mappedProduct);

          ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);

          return savedProductDTO;
  
      }

      @Override
      public ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder,String categoryName,String keyword){
            
    	    Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")            
                                          ?Sort.by(sortBy).ascending()
                                          :Sort.by(sortBy).descending();
    	  
            Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
            

            Specification<Product> spec = Specification.where(null) ; // Specification:-> Preparing the filter recepie starting with the empty filter

            if(keyword!=null && !keyword.isEmpty()){
                       spec =  spec.and((root,query,criteriaBuilder)->
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + keyword.toLowerCase()+"%")
                                );
            }

            if (categoryName != null && !categoryName.isEmpty()) {
	            spec = spec.and((root, query, criteriaBuilder) ->
	                    criteriaBuilder.like(root.get("category").get("categoryName"), categoryName));
	        }

            Page<Product> productPage = productRepository.findAll(spec,pageDetails); // executing the query with the given pagination and sorting

            // Getting the list of products from the productPage
            List<Product> products = productPage.getContent();

            if(products.isEmpty()){
                  throw new ApiException("No product exists");
            }

            // Mapping from product to productDTO
            List<ProductDTO> fetchedProductDTO = products.stream().map((product) -> {
                 ProductDTO productDTO =  modelMapper.map(product,ProductDTO.class);
                 productDTO.setImage(constructImageUrl(product.getImage())); // Construction of the image is pending here 
                 return productDTO;
                        }).collect(Collectors.toList());

            ProductResponse productResponse = new ProductResponse();
            
            productResponse.setContent(fetchedProductDTO);
            productResponse.setPageNumber(productPage.getNumber());
            productResponse.setPageSize(productPage.getSize());
            productResponse.setTotalElements(productPage.getTotalElements());
            productResponse.setTotalPages(productPage.getTotalPages());
            productResponse.setLast(productPage.isLast());
            return productResponse;      
      }
      

    @Override
	public  ProductResponse searchProductByCategory(Long categoryId){
		Category category = categoryRepository.findById(categoryId)	
													.orElseThrow(() -> new ResourceNotFoundException("category","categoryId",categoryId));


		
		 List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);

		 if(products.isEmpty()){
				throw new ApiException("No product exists in the above category");
		 }

		 List<ProductDTO> fetchedProductDTO =  products.stream().map((product) ->{
			 return modelMapper.map(product,ProductDTO.class);
		 }).collect(Collectors.toList());
		 

		 ProductResponse productResponse = new ProductResponse();
		 productResponse.setContent(fetchedProductDTO);
		 return productResponse;
		  
		}

    @Override
      public  ProductResponse searchProductByKeyword(String keyword){
			List<Product> products = productRepository.findByProductNameLikeIgnoreCase("%"+ keyword + "%");

			 if(products.isEmpty()){
					throw new ApiException("No product exists in the above category");
			 }
	
			 List<ProductDTO> fetchedProductDTO =  products.stream().map((product) ->{
				 return modelMapper.map(product,ProductDTO.class);
			 }).collect(Collectors.toList());
	
			 ProductResponse productResponse = new ProductResponse();
			 productResponse.setContent(fetchedProductDTO);
			 return productResponse;

		}	

        
    @Override 
     public ProductDTO deleteProduct(Long productId){

			Product productToBeDeleted = productRepository.findById(productId)
														.orElseThrow(() -> new ResourceNotFoundException("product","productId",productId));

		   	productRepository.deleteById(productId);

			return modelMapper.map(productToBeDeleted, ProductDTO.class);
			
			
		}	

       
    @Override
		public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
	    Product productToBeUpdated = productRepository.findById(productId)
	        .orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));
	
	    Product product = modelMapper.map(productDTO, Product.class);
	    product.setProductId(productId);
	
	    Product updatedProduct = productRepository.save(product);
	
	    ProductDTO updatedProductDTO = modelMapper.map(updatedProduct, ProductDTO.class);
	
	    return updatedProductDTO;
	}

	 @Override
      public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
		 
			Product productFromDb = productRepository.findById(productId)
												.orElseThrow(() -> new ResourceNotFoundException("product","productId",productId));
			

		  	// Uploading the image to the server , Getting the filename of the uploaded image 
			String filename = fileService.uploadImage(path,image);

		  	// Updating the new file name to the product
		     productFromDb.setImage(filename);

			  	// Saving the updatedproduct
			  Product updatedProduct = productRepository.save(productFromDb);
	
			  return modelMapper.map(updatedProduct,ProductDTO.class);
		}

     
      
      
      public String constructImageUrl(String imageName){
          return imageName.endsWith("/")?imageBaseUrl+ imageName : imageBaseUrl + "/" + imageName;
                
    }
      
    
} 
