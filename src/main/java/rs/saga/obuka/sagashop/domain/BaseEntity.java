package rs.saga.obuka.sagashop.domain;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@SuppressWarnings({"unused", "WeakerAccess"})
@MappedSuperclass
public abstract class BaseEntity<K extends Serializable> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private K id;

    public K getId(){
        return  id;
    }

    public void setId(K id){
        this.id = id;
    }
}
