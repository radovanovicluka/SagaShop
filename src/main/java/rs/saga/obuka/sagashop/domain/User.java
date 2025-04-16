package rs.saga.obuka.sagashop.domain;

import lombok.*;
import rs.saga.obuka.sagashop.audit.Audit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Audit<Long> {

    @Column(unique = true, nullable = false)
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private PayPalAccount payPalAccount;

    @OneToMany(mappedBy = "user")
    private List<ShoppingCart> shoppingCarts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }

    public void setAccount(PayPalAccount payPalAccount) {
        this.payPalAccount = payPalAccount;
        payPalAccount.setUser(this);
    }

}
