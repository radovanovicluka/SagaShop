package rs.saga.obuka.sagashop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import rs.saga.obuka.sagashop.dto.category.UpdateCategoryCmd;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductCmd {

    private Long id;
    private BigDecimal price;
    private String name;
    private String description;
    private Integer quantity;
    private List<UpdateCategoryCmd> categories;

}
