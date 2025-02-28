package rs.saga.obuka.sagashop.domain;

import javax.validation.constraints.NotNull;
import lombok.*;

import javax.persistence.*;
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

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name) && Objects.equals(getId(), category.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name);
    }
}
