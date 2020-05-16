package red.softn.standard.db.ejb;

import red.softn.standard.db.api.ProfilesDI;
import red.softn.standard.db.dao.ProfilesDAO;
import red.softn.standard.db.dto.ProfilesDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Stateless
public class ProfilesD implements ProfilesDI {
    
    @Inject
    private ProfilesDAO profilesDAO;
    
    @Override
    public List<ProfilesDTO> find(EntityManager entityManager, ProfilesDTO dto) throws Exception {
        return profilesDAO.find(entityManager, dto);
    }
    
    @Override
    public ProfilesDTO findById(EntityManager entityManager, Integer id) throws Exception {
        return this.profilesDAO.findById(entityManager, id);
    }
    
    @Override
    public ProfilesDTO insert(EntityManager entityManager, ProfilesDTO dto) throws Exception {
        return this.profilesDAO.insert(entityManager, dto);
    }
    
    @Override
    public ProfilesDTO update(EntityManager entityManager, ProfilesDTO dto) throws Exception {
        return this.profilesDAO.update(entityManager, dto);
    }
    
    @Override
    public void deleteById(EntityManager entityManager, Integer id) throws Exception {
        this.profilesDAO.deleteById(entityManager, id);
    }
}
