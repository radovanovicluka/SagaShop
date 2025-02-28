package rs.saga.obuka.sagashop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.saga.obuka.sagashop.audit.Audit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ShoppingCart extends Audit<Long> {

    @Column
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

}
