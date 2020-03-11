package red.softn.standard.db.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import red.softn.standard.common.ReflectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QueryEntityBuilder<E> {
    
    @Setter(value = AccessLevel.PRIVATE)
    private Builder<E> builder;
    
    private QueryEntityBuilder() {
    }
    
    public static <E> Builder<E> create(EntityManager entityManager, Class<E> entityClass, E entity) {
        return new Builder<>(new QueryEntityBuilder<>(), entityManager, entityClass, entity);
    }
    
    public List<E> getResultList() {
        return this.builder.createQuery()
                           .getResultList();
    }
    
    public static class Builder<E> {
        
        private final Class<E> entityClass;
        
        private EntityManager entityManager;
        
        private E entity;
        
        private CriteriaBuilder criteriaBuilder;
        
        @Getter(value = AccessLevel.PRIVATE)
        private CriteriaQuery<E> criteriaQuery;
        
        private Root<E> root;
        
        private QueryEntityBuilder<E> queryEntityBuilder;
        
        protected Builder(QueryEntityBuilder<E> queryEntityBuilder, EntityManager entityManager, Class<E> entityClass, E entity) {
            this.queryEntityBuilder = queryEntityBuilder;
            this.entity = entity;
            this.entityClass = entityClass;
            this.entityManager = entityManager;
            this.criteriaBuilder = entityManager.getCriteriaBuilder();
            this.criteriaQuery = this.criteriaBuilder.createQuery(entityClass);
            this.root = this.criteriaQuery.from(this.criteriaQuery.getResultType());
        }
        
        protected TypedQuery<E> createQuery() {
            this.criteriaQuery.select(this.root);
            this.criteriaQuery.where(getPredicates());
            
            return this.entityManager.createQuery(this.criteriaQuery);
        }
        
        public QueryEntityBuilder<E> build() {
            this.queryEntityBuilder.setBuilder(this);
            
            return this.queryEntityBuilder;
        }
        
        private Predicate[] getPredicates() {
            return ReflectionUtils.objectToMap(this.entityClass, this.entity)
                                  .entrySet()
                                  .stream()
                                  .filter(entry -> Objects.nonNull(entry.getValue()))
                                  .map(this::mapPredicate)
                                  .toArray(Predicate[]::new);
        }
        
        private Predicate mapPredicate(Map.Entry<String, Object> entry) {
            String key   = entry.getKey();
            Object value = entry.getValue();
            
            return this.criteriaBuilder.equal(this.root.get(key), value);
        }
        
    }
}
