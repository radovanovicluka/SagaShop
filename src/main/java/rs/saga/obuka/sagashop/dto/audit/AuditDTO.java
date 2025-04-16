package rs.saga.obuka.sagashop.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditDTO implements Serializable {

    private String createdBy;
    private Date creationDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private Long version;

}
