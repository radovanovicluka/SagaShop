package rs.saga.obuka.sagashop.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseEntity<Long> {

    @Column
    @NotNull
    private Integer quantity;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(quantity, item.quantity) && Objects.equals(product, item.product) && Objects.equals(shoppingCart, item.shoppingCart);
    }

    @Override
    public int hashCode() {
        return 12;
    }
}
