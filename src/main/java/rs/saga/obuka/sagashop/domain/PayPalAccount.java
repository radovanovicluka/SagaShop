package rs.saga.obuka.sagashop.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="paypal_account")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayPalAccount extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    @NotNull
    private String accountNumber;

    @NotNull
    private BigDecimal budget;

    @NotNull
    private String language;

    @NotNull
    private LocalDate expiresOn;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="postalCode", column = @Column(name = "postal_code"))
    })
    private Address billingAddress;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="userID")
    @NotNull
    private User user;
}
