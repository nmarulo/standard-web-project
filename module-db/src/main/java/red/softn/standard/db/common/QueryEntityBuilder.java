package red.softn.standard.db.common;

import lombok.AccessLevel;
import lombok.Setter;
import red.softn.standard.common.ReflectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QueryEntityBuilder<E> {
    
    @Setter(value = AccessLevel.PRIVATE)
    private Builder<E> builder;
    
    private QueryEntityBuilder() {
    }
    
    public static <E> QueryEntityBuilder<E> create(EntityManager entityManager, Class<E> entityClass, E entity) {
        return new Builder<>(new QueryEntityBuilder<>(), entityManager, entityClass, entity).build();
    }
    
    public List<E> getList() {
        return this.builder.list()
                           .getResultList();
    }
    
    public Long getCount() {
        return this.builder.count()
                           .getSingleResult();
    }
    
    public static class Builder<E> {
        
        private final Class<E> entityClass;
        
        private EntityManager entityManager;
        
        private E entity;
        
        private CriteriaBuilder criteriaBuilder;
        
        private CriteriaQuery<E> criteriaQuery;
        
        private Root<E> root;
        
        private QueryEntityBuilder<E> queryEntityBuilder;
        
        protected Builder(QueryEntityBuilder<E> queryEntityBuilder, EntityManager entityManager, Class<E> entityClass, E entity) {
            this.queryEntityBuilder = queryEntityBuilder;
            this.entity = entity;
            this.entityClass = entityClass;
            this.entityManager = entityManager;
            this.criteriaBuilder = entityManager.getCriteriaBuilder();
        }
        
        protected TypedQuery<E> list() {
            this.criteriaQuery = this.criteriaBuilder.createQuery(entityClass);
            this.root = this.criteriaQuery.from(this.criteriaQuery.getResultType());
            
            return createQuery(this.criteriaQuery, this.root);
        }
        
        protected TypedQuery<Long> count() {
            CriteriaQuery<Long> criteriaQuery = this.criteriaBuilder.createQuery(Long.class);
            this.root = criteriaQuery.from(this.entityClass);
            
            return createQuery(criteriaQuery, this.criteriaBuilder.count(this.root));
        }
        
        protected <R> TypedQuery<R> createQuery(CriteriaQuery<R> criteriaQuery, Selection<R> selection) {
            criteriaQuery.select(selection);
            criteriaQuery.where(getPredicates());
            
            return this.entityManager.createQuery(criteriaQuery);
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
            Path<String> key   = this.root.get(entry.getKey());
            Object       value = entry.getValue();
            
            if (value instanceof String) {
                return this.criteriaBuilder.like(key, "%" + value + "%");
            }
            
            return this.criteriaBuilder.equal(key, value);
        }
        
    }
}
