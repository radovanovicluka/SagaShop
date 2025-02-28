package rs.saga.obuka.sagashop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductProjection {

    private Long id;
    private BigDecimal price;
    private String name;
    private Integer quantity;
    private Long categoryId;
    private String categoryName;

}
