package com.example.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Shortcut for @Getter/@Setter/@ToString 
@NoArgsConstructor // No Argument constructor
@AllArgsConstructor // Generates constructors with all the fields as the parameter 
public class Product{
      @Id
      @GeneratedValue(strategy=GenerationType.IDENTITY)
      Integer productId;

      @NotBlank
      String productName;

      private String image;

      @NotBlank
      @Size(min=5 , message = "Product description must have atleast 5 characters")
      String description;
  
      Integer quantity;
      Double price;
      Double discount;
      Double specialPrice;

      @ManyToOne // Many products belong to one Category
      @JoinColumn(name="category_id") // A foreign key corresponding to the parent table Category will be created in the Child table Product with the name as category_id"
      private Category category;
      
  
}
