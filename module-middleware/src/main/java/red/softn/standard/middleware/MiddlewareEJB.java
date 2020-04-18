package red.softn.standard.middleware;

import red.softn.standard.db.common.DbEJB;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

public abstract class MiddlewareEJB {
    
    @PersistenceUnit(unitName = "SoftnPU")
    private EntityManagerFactory entityManagerFactory;
    
    EntityManager entityManager;
    
    protected EntityManager getConnection() {
        this.entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = this.entityManager.getTransaction();
        transaction.begin();
        
        return this.entityManager;
    }
    
    protected void closeConnection() {
        if (this.entityManager != null && this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }
    
}
