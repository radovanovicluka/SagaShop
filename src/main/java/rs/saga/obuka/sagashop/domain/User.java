package rs.saga.obuka.sagashop.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String password;

    @OneToOne( mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private PayPalAccount payPalAccount;

    public void setAccount( PayPalAccount payPalAccount ) {
        this.payPalAccount = payPalAccount;
        payPalAccount.setUser( this );
    }

}
