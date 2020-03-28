package red.softn.standard.middleware;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

public abstract class AMiddleware {
    
    @PersistenceUnit(unitName = "SoftnPU")
    private EntityManagerFactory entityManagerFactory;
    
    protected EntityManager getConnection() {
        return entityManagerFactory.createEntityManager();
    }
}
