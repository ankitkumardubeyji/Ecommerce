
@Entity
@Data // Shortcut for @Getter/@Setter/@ToString 
@NoArgsConstructor // No Argument constructor
@AllArgsConstructor // Generates constructors with all the fields as the parameter 
class Product{
      @Id
      @GeneratedValue(StrategyType="GenerationType.IDENTITY")
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

      @ManyToOne // Many products belong to one Category
      @JoinColumn(name="category_id) // A foreign key corresponding to the parent table Category will be created in the Child table Product with the name as category_id
      private Category category;
      
  
}
