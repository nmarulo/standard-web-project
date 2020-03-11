package red.softn.standard.db.api;

import red.softn.standard.db.dto.UsersDTO;

import javax.persistence.EntityManager;
import java.util.List;

public interface UsersDI {
    
    UsersDTO findById(EntityManager entityManager, Integer id) throws Exception;
    
    List<UsersDTO> find(EntityManager entityManager, UsersDTO dto) throws Exception;
    
    UsersDTO insert(EntityManager entityManager, UsersDTO dto) throws Exception;
    
    UsersDTO update(EntityManager entityManager, UsersDTO dto) throws Exception;
    
    void delete(EntityManager entityManager, UsersDTO dto) throws Exception;
    
    void deleteById(EntityManager entityManager, Integer id) throws Exception;
}
