package rs.saga.obuka.sagashop.builder;

import rs.saga.obuka.sagashop.domain.PayPalAccount;
import rs.saga.obuka.sagashop.domain.User;

public class UserBuilder {

    public static User userLuka() {
        return User.builder()
                .name("Luka")
                .surname("Radovanovic")
                .password("sifra")
                .build();
    }

    public static User userAdmin() {
        return User.builder().username("user")
                .password("user").name("user").surname("user")
                .build();
    }

    public static User userNikola(PayPalAccount paypalAccount) {
        return User.builder().username("nikola")
                .password("nikola").name("Nikola").surname("Ninovic")
                .payPalAccount(paypalAccount)
                .build();

    }
}