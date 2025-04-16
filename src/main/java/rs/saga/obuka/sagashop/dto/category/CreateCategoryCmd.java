package rs.saga.obuka.sagashop.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.saga.obuka.sagashop.dto.product.CreateProductFromCategoryCmd;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Ana Dedović
 * Date: 28.06.2021.
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryCmd implements Serializable {

    private String categoryName;
    private String description;
    //    Novi Product
    private List<CreateProductFromCategoryCmd> products;
    //    Vec postojeci produkti
    private List<Long> productIds;

}
