package rs.saga.obuka.sagashop.audit;

import lombok.Data;
import rs.saga.obuka.sagashop.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@MappedSuperclass
public abstract class Version<PK extends Serializable> extends BaseEntity<PK> {

    @Column
    @javax.persistence.Version
    private Long version = 1L;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
