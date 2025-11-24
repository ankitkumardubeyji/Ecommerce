
@Entity
class Product{
      @Id
      @GeneratedValue(StrategyType="GenerationType.IDENTITY")
      Integer productId;

      @NotBlank
      String productName;

      @NotBlank
      @Size(min=5 , message = "Product description must have atleast 5 characters")
      String description;
  
      Integer quantity;
  
      Integer price;
      Integer discount;
      Integer specialPrice;
  
}
