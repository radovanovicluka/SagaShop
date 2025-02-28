package rs.saga.obuka.sagashop.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity<Long> {

    @NotNull
    private BigDecimal price;

    @NotNull
    private String name;

    @Column
    private String description;

    @NotNull
    private Integer quantity;

    @ManyToMany( fetch = FetchType.LAZY, cascade = CascadeType.MERGE )
    @JoinTable(
            name="category_product",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    void addCategory(Category category) {
        categories.add(category);
    }

    void removeCategory(Category category) {
        categories.remove(category);
    }

}
