

@Data // Shortcut for @Getter/@Setter/@ToString 
@NoArgsConstructor // No Argument constructor
@AllArgsConstructor // Generates constructors with all the fields as the parameter 
class Product{
     
      Integer productId;

      @NotBlank
      String productName;

      private String image;

      @NotBlank
      @Size(min=5 , message = "Product description must have atleast 5 characters")
      String description;
  
      Integer quantity;
      Integer price;
      Integer discount;
      Integer specialPrice;
  
}
