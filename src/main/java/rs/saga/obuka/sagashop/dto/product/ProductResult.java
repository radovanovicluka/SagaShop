package rs.saga.obuka.sagashop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.saga.obuka.sagashop.dto.audit.AuditDTO;
import rs.saga.obuka.sagashop.dto.category.CategoryResult;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResult implements Serializable {

    private Long id;
    private BigDecimal price;
    private String name;
    private String description;
    private Integer quantity;
    private List<CategoryResult> categories;
    private AuditDTO audit = new AuditDTO();

}
