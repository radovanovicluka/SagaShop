package rs.saga.obuka.sagashop.audit;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rs.saga.obuka.sagashop.domain.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class Audit<PK extends Serializable> extends Version<PK> {

    @CreatedBy
    @ManyToOne
    protected User createdBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    protected Date creationDate;

    @LastModifiedBy
    @ManyToOne
    protected User lastModifiedBy;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    protected Date lastModifiedDate;

}
