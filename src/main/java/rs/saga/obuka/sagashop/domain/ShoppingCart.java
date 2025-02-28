package rs.saga.obuka.sagashop.domain;

import lombok.*;
import rs.saga.obuka.sagashop.audit.Audit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart extends Audit<Long> {

    @Column(unique = true)
    @NotNull
    private String name;

    @Column
    @NotNull
    private BigDecimal price = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private Status status = Status.NEW;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "shoppingCart")
    private List<Item> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingCart)) return false;
        if (!super.equals(o)) return false;
        ShoppingCart cart = (ShoppingCart) o;
        return Objects.equals(name, cart.name) && (getId() == null || cart.getId() == null || Objects.equals(getId(), cart.getId()));
    }

    @Override
    public int hashCode() {
        return 16;
    }
}
