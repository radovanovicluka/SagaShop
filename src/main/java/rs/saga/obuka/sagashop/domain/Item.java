package rs.saga.obuka.sagashop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
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

}
