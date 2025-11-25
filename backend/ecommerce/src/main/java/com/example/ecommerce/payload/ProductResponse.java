package com.example.ecommerce.payload;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse{

      List<ProductDTO> content;
      Integer pageNumber;
      Integer pageSize;
      Integer totalElements;
      Integer totalPages;
      boolean isLast;
}
