package red.softn.standard.db.dao;

import red.softn.standard.common.ReflectionUtils;
import red.softn.standard.db.common.QueryEntityBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class CrudDAO<T, ID> {
    
    public T insert(EntityManager entityManager, T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        
        return entity;
    }
    
    public List<T> find(EntityManager entityManager, T entity) throws Exception {
        return QueryEntityBuilder.create(entityManager, getEntityClass(), entity)
                                 .build()
                                 .getResultList();
    }
    
    public T findById(EntityManager entityManager, ID id) throws Exception {
        return entityManager.find(getEntityClass(), id);
    }
    
    public T update(EntityManager entityManager, T entity) {
        return entityManager.merge(entity);
    }
    
    public void delete(EntityManager entityManager, T entity) throws Exception {
        find(entityManager, entity).forEach(entityManager::remove);
    }
    
    public void deleteById(EntityManager entityManager, ID id) throws Exception {
        entityManager.remove(findById(entityManager, id));
    }
    
    private Class<T> getEntityClass() throws Exception {
        return (Class<T>) ReflectionUtils.getGenericSuperClass(getClass(), 0);
    }
}
