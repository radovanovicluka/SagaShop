package rs.saga.obuka.sagashop.builder;

import rs.saga.obuka.sagashop.domain.Role;

public class RoleBuilder {

    public static Role roleUser() {
        return Role.builder()
                .name("USER")
                .build();
    }

    public static Role roleAdmin() {
        return Role.builder()
                .name("ADMIN")
                .build();
    }

}
