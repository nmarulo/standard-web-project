package red.softn.standard.db.common;

import javax.persistence.EntityManager;

public interface DefaultDI {
    
    void setEntityManager(EntityManager entityManager);
}
