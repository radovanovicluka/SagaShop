package rs.saga.obuka.sagashop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductCmd {

    private Long id;
    private BigDecimal price;
    private String name;
    private String description;
    private Integer quantity;
    //    Id za dodavanje kategorija
    private List<Long> addCategoryIds;
    //    Id za uklanjanje kategorija
    private List<Long> removeCategoryIds;

}
