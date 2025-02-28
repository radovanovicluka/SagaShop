package rs.saga.obuka.sagashop.domain;

import javax.validation.constraints.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * @author: Ana Dedović
 * Date: 24.06.2021.
 **/
@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class Category extends BaseEntity<Long> {

    @Column(nullable = false, name = "category_name")
    @NotNull
    private String categoryName;

    @Column
    private String description;

    @ManyToMany(mappedBy = "categories", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Product> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(categoryName, category.categoryName) && Objects.equals(getId(), category.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), categoryName);
    }
}
