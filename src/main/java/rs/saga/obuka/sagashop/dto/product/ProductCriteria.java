package rs.saga.obuka.sagashop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.saga.obuka.sagashop.dto.paging.PagingRequest;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCriteria {

    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String description;
    private Integer minQuantity;
    private Integer maxQuantity;
    private String categoryName;
    private PagingRequest pagingRequest;

}
