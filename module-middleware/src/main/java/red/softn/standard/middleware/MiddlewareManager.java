package red.softn.standard.middleware;

import lombok.AccessLevel;
import lombok.Getter;
import red.softn.standard.common.GsonUtil;
import red.softn.standard.db.common.DefaultDI;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import java.util.*;

public class MiddlewareManager {
    
    public static <T> MiddlewareHandle<T> init(String unitName, DefaultDI... defaultDI) {
        return new MiddlewareHandle<T>().init(unitName, defaultDI);
    }
    
    public static class MiddlewareEntityManager<T> {
        
        private final MiddlewareHandle<T> middlewareHandle;
        
        private MiddlewareEntityManager(MiddlewareHandle<T> middlewareHandle) {
            this.middlewareHandle = middlewareHandle;
        }
        
        protected void commit() {
            this.middlewareHandle.getEntityManagerMap()
                                 .values()
                                 .stream()
                                 .map(EntityManager::getTransaction)
                                 .filter(EntityTransaction::isActive)
                                 .forEach(EntityTransaction::commit);
        }
        
        protected void rollback() {
            this.middlewareHandle.getEntityManagerMap()
                                 .values()
                                 .stream()
                                 .map(EntityManager::getTransaction)
                                 .filter(EntityTransaction::isActive)
                                 .forEach(EntityTransaction::rollback);
            
        }
    }
    
    public static class MiddlewareHandle<T> {
        
        @PersistenceUnit(unitName = "SoftnPU")
        private EntityManagerFactory entityManagerFactory;
        
        @Getter(value = AccessLevel.PRIVATE)
        private final Map<String, EntityManager> entityManagerMap;
        
        private T get;
        
        private List<T> list;
        
        private final MiddlewareEntityManager<T> middlewareEntityManager;
        
        protected MiddlewareHandle() {
            this.entityManagerMap = new HashMap<>();
            this.middlewareEntityManager = new MiddlewareEntityManager<>(this);
        }
        
        public MiddlewareHandle<T> init(String unitName, DefaultDI... defaultDI) {
            Arrays.stream(defaultDI)
                  .forEach(value -> value.setEntityManager(getEntityManager(unitName)));
            
            return this;
        }
        
        public MiddlewareHandle<T> handle(ThrowingFunction<MiddlewareEntityManager<T>, List<T>> supplier) throws Exception {
            this.entityManagerMap.values()
                                 .stream()
                                 .map(EntityManager::getTransaction)
                                 .forEach(EntityTransaction::begin);
            
            try {
                list = supplier.apply(this.middlewareEntityManager);
            } finally {
                this.middlewareEntityManager.rollback();
            }
            
            return this;
        }
    
        public MiddlewareHandle<T> handle(ThrowingSupplier<List<T>> supplier) throws Exception {
            this.entityManagerMap.values()
                                 .stream()
                                 .map(EntityManager::getTransaction)
                                 .forEach(EntityTransaction::begin);
        
            try {
                list = supplier.get();
            } finally {
                this.middlewareEntityManager.rollback();
            }
        
            return this;
        }
        
        public T get() {
            closeConnection();
            
            return this.get;
        }
        
        public <O> O get(Class<O> clazz) {
            closeConnection();
            
            return GsonUtil.convertObjectTo(this.get, clazz);
        }
        
        public List<T> list() {
            closeConnection();
            
            return this.list;
        }
        
        public <O> List<O> list(Class<O[]> clazz) {
            closeConnection();
            
            return GsonUtil.convertObjectListTo(this.list, clazz);
        }
        
        private EntityManager getEntityManager(String unitName) {
            if (!this.entityManagerMap.containsKey(unitName)) {
                this.entityManagerMap.put(unitName, entityManagerFactory.createEntityManager());
            }
            
            return this.entityManagerMap.get(unitName);
        }
        
        private void closeConnection() {
            this.entityManagerMap.values()
                                 .stream()
                                 .filter(Objects::nonNull)
                                 .filter(EntityManager::isOpen)
                                 .forEach(EntityManager::close);
            this.entityManagerMap.clear();
        }
    }
    
    @FunctionalInterface
    public interface ThrowingRunnable {
        
        void run() throws Exception;
    }
    
    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        
        T get() throws Exception;
    }
    
    @FunctionalInterface
    public interface ThrowingConsumer<T> {
        
        void accept(T t) throws Exception;
    }
    
    @FunctionalInterface
    public interface ThrowingFunction<T, R> {
        
        R apply(T t) throws Exception;
    }
}
