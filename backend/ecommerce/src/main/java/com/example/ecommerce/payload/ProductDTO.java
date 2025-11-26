package com.example.ecommerce.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Shortcut for @Getter/@Setter/@ToString 
@NoArgsConstructor // No Argument constructor
@AllArgsConstructor // Generates constructors with all the fields as the parameter 

public class ProductDTO{
     
      Integer productId;

      @NotBlank
      String productName;

      private String image;

      @NotBlank
      @Size(min=5 , message = "Product description must have atleast 5 characters")
      String description;
  
      Integer quantity;
      double price;
      double discount;
      double specialPrice;
  
}
