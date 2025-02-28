package rs.saga.obuka.sagashop.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity<Long> {

    @Column( unique=true, nullable=false )
    @NotNull
    private String username;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private String surname;

    @Column
    @NotNull
    private String password;

    @OneToOne( mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private PayPalAccount payPalAccount;

    @OneToMany(mappedBy = "user")
    private List<ShoppingCart> shoppingCarts;

    public void setAccount( PayPalAccount payPalAccount ) {
        this.payPalAccount = payPalAccount;
        payPalAccount.setUser( this );
    }

}
