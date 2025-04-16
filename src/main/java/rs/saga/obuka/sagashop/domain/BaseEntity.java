package rs.saga.obuka.sagashop.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@SuppressWarnings({"unused", "WeakerAccess"})
@MappedSuperclass
public abstract class BaseEntity<K extends Serializable> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private K id;

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }
}
