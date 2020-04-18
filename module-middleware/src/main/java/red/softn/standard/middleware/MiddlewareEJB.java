package red.softn.standard.middleware;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

public abstract class MiddlewareEJB {
    
    @PersistenceUnit(unitName = "SoftnPU")
    private EntityManagerFactory entityManagerFactory;
    
    protected EntityManager getConnection() {
        return entityManagerFactory.createEntityManager();
    }
    
    protected void closeConnection() {
        if (this.entityManagerFactory != null && this.entityManagerFactory.isOpen()) {
            this.entityManagerFactory.close();
        }
    }
    
    
}
