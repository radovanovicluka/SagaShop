package rs.saga.obuka.sagashop.dto.paypalaccount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.saga.obuka.sagashop.domain.Address;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayPalAccountInfo {

    private Long id;
    private String accountNumber;
    private BigDecimal budget;
    private String language;
    private LocalDate expiresOn;
    private Address billingAddress;

}
