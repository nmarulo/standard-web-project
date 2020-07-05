package red.softn.standard.db.common;

import javax.persistence.EntityManager;

public class DefaultD implements DefaultDI {
    
    private EntityManager entityManager;
    
    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
