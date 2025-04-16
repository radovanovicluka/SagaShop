package rs.saga.obuka.sagashop.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.saga.obuka.sagashop.dto.audit.AuditDTO;

import java.io.Serializable;

import java.io.Serializable;

/**
 * @author: Ana Dedović
 * Date: 28.06.2021.
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResult implements Serializable {

    private Long id;
    private String categoryName;
    private String description;
    private AuditDTO audit = new AuditDTO();

}
