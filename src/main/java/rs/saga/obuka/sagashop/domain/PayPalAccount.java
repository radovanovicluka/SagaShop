package rs.saga.obuka.sagashop.domain;

import lombok.*;
import rs.saga.obuka.sagashop.audit.Audit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "paypal_account")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayPalAccount extends Audit<Long> {

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
            @AttributeOverride(name = "postalCode", column = @Column(name = "postal_code"))
    })
    private Address billingAddress;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userID")
    @NotNull
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PayPalAccount)) return false;
        if (!super.equals(o)) return false;
        PayPalAccount that = (PayPalAccount) o;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return 13;
    }
}
