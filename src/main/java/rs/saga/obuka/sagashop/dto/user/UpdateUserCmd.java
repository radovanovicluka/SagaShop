package rs.saga.obuka.sagashop.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.saga.obuka.sagashop.dto.paypalaccount.UpdatePayPalAccountCmd;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserCmd {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;

}
