package rs.saga.obuka.sagashop.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.saga.obuka.sagashop.dto.audit.AuditDTO;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResult implements Serializable {

    private Long id;
    private String username;
    private String name;
    private String surname;
    private AuditDTO audit = new AuditDTO();

}
