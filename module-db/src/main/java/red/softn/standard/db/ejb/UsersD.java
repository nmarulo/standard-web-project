package red.softn.standard.db.ejb;

import red.softn.standard.db.api.UsersDI;
import red.softn.standard.db.common.DefaultD;
import red.softn.standard.db.dao.UsersDAO;
import red.softn.standard.db.dto.UsersDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Stateless
public class UsersD extends DefaultD implements UsersDI {
    
    @Inject
    private UsersDAO usersDAO;
    
    @Override
    public UsersDTO findById(EntityManager entityManager, Integer id) throws Exception {
        return this.usersDAO.findById(entityManager, id);
    }
    
    @Override
    public List<UsersDTO> find(EntityManager entityManager, UsersDTO dto) throws Exception {
        return this.usersDAO.find(entityManager, dto);
    }
    
    @Override
    public UsersDTO insert(EntityManager entityManager, UsersDTO dto) throws Exception {
        if (dto.getUserRegistered() == null) {
            dto.setUserRegistered(new Date());
        }
        
        return this.usersDAO.insert(entityManager, dto);
    }
    
    @Override
    public UsersDTO update(EntityManager entityManager, UsersDTO dto) throws Exception {
        return this.usersDAO.update(entityManager, dto);
    }
    
    @Override
    public void deleteById(EntityManager entityManager, Integer id) throws Exception {
        this.usersDAO.deleteById(entityManager, id);
    }
}
