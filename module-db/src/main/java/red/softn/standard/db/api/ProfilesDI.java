package red.softn.standard.db.api;

import red.softn.standard.db.common.DefaultDI;
import red.softn.standard.db.dto.ProfilesDTO;

import javax.persistence.EntityManager;
import java.util.List;

public interface ProfilesDI extends DefaultDI {
    
    List<ProfilesDTO> find(EntityManager entityManager, ProfilesDTO dto) throws Exception;
    
    ProfilesDTO findById(EntityManager entityManager, Integer id) throws Exception;
    
    ProfilesDTO insert(EntityManager entityManager, ProfilesDTO dto) throws Exception;
    
    ProfilesDTO update(EntityManager entityManager, ProfilesDTO dto) throws Exception;
    
    void deleteById(EntityManager entityManager, Integer id) throws Exception;
}
