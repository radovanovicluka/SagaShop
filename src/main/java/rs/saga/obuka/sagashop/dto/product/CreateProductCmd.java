package rs.saga.obuka.sagashop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.saga.obuka.sagashop.dto.category.CreateCategoryFromProductCmd;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductCmd implements Serializable {

    private BigDecimal price;
    private String name;
    private String description;
    private Integer quantity;
    //    Nove kategorje
    private List<CreateCategoryFromProductCmd> categories;
    //    Vec postojece kategorije
    private List<Long> categoryIds;

}
