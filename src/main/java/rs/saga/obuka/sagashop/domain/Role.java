package rs.saga.obuka.sagashop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity<Long> implements GrantedAuthority {

    @NotNull
    @Column(unique = true)
    private String name;

    public String getAuthority() {
        return name;
    }

}
