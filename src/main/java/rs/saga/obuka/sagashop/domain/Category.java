package rs.saga.obuka.sagashop.domain;

import lombok.*;
import rs.saga.obuka.sagashop.audit.Audit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
public class Category extends Audit<Long> {

    @Column(nullable = false, name = "category_name", unique = true)
    @NotNull
    private String categoryName;

    @Column
    private String description;

    @ManyToMany(mappedBy = "categories", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Product> products = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(categoryName, category.categoryName);
    }

    @Override
    public int hashCode() {
        return 11;
    }
}
