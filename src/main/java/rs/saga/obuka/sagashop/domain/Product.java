package rs.saga.obuka.sagashop.domain;

import lombok.*;
import rs.saga.obuka.sagashop.audit.Audit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends Audit<Long> {

    @NotNull
    @Column
    private BigDecimal price;

    @NotNull
    @Column(unique = true)
    private String name;

    @Column
    private String description;

    @NotNull
    private Integer quantity;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "category_product",
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return 14;
    }

}
