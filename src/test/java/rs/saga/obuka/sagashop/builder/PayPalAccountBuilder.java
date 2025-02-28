package rs.saga.obuka.sagashop.builder;

import rs.saga.obuka.sagashop.domain.Address;
import rs.saga.obuka.sagashop.domain.PayPalAccount;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PayPalAccountBuilder {

    public static PayPalAccount account1Build() {
        return PayPalAccount.builder()
                .user(UserBuilder.userAdmin())
                .accountNumber("8465 6548 9875 3216")
                .budget(new BigDecimal(123.56))
                .expiresOn(LocalDate.now())
                .language("SRB")
                .billingAddress(new Address("BG", "SRB", "12000"))
                .build();
    }

    public static PayPalAccount account2Build() {
        return PayPalAccount.builder()
                .user(UserBuilder.userAdmin())
                .accountNumber("4891 9865 8713 8258")
                .budget(new BigDecimal(12300.56))
                .expiresOn(LocalDate.now())
                .language("BUL")
                .billingAddress(new Address("SF", "BUL", "18100"))
                .build();
    }

    public static PayPalAccount account3Build() {
        return PayPalAccount.builder()
                .user(UserBuilder.userAdmin())
                .accountNumber("2424 9631 2154 8975")
                .budget(new BigDecimal(876908.98))
                .expiresOn(LocalDate.now())
                .language("ENG")
                .billingAddress(new Address("NY", "USA", "92000"))
                .build();
    }

}
