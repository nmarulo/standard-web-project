package red.softn.standard.middleware.ejb;

import red.softn.standard.common.GsonUtil;
import red.softn.standard.db.api.ProfilesDI;
import red.softn.standard.db.api.UsersDI;
import red.softn.standard.db.dto.ProfilesDTO;
import red.softn.standard.db.dto.UsersDTO;
import red.softn.standard.middleware.MiddlewareEJB;
import red.softn.standard.middleware.api.UsersMI;
import red.softn.standard.objects.pojo.Profile;
import red.softn.standard.objects.request.UserRequest;
import red.softn.standard.objects.response.ProfileResponse;
import red.softn.standard.objects.response.UserFormCreateUpdateResponse;
import red.softn.standard.objects.response.UserResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class UsersM extends MiddlewareEJB implements UsersMI {
    
    @Inject
    private UsersDI usersDI;
    
    @Inject
    private ProfilesDI profilesDI;
    
    @Override
    public List<UserResponse> search(UserRequest request) throws Exception {
        try {
            List<UsersDTO> dtoList = this.usersDI.find(getConnection(), GsonUtil.convertObjectTo(request, UsersDTO.class));
            
            return GsonUtil.convertObjectListTo(dtoList, UserResponse[].class);
        } finally {
            closeConnection();
        }
    }
    
    @Override
    public UserResponse getById(Integer request) throws Exception {
        try {
            UsersDTO dto = getUser(request);
            
            return convert(dto);
        } finally {
            closeConnection();
        }
    }
    
    @Override
    public UserResponse post(UserRequest request) throws Exception {
        try {
            UsersDTO dto = this.usersDI.insert(getConnection(), GsonUtil.convertObjectTo(request, UsersDTO.class));
            commit();
            
            return convert(dto);
        } catch (Exception ex) {
            rollback();
            throw ex;
        } finally {
            closeConnection();
        }
    }
    
    @Override
    public UserResponse put(UserRequest request) throws Exception {
        try {
            UsersDTO dto = this.usersDI.update(getConnection(), GsonUtil.convertObjectTo(request, UsersDTO.class));
            commit();
            
            return convert(dto);
        } catch (Exception ex) {
            rollback();
            throw ex;
        } finally {
            closeConnection();
        }
    }
    
    @Override
    public void deleteById(Integer request) throws Exception {
        try {
            this.usersDI.deleteById(getConnection(), request);
            commit();
        } catch (Exception ex) {
            rollback();
            throw ex;
        } finally {
            closeConnection();
        }
    }
    
    @Override
    public UserFormCreateUpdateResponse formCreateUpdate(Integer request) throws Exception {
        try {
            UserFormCreateUpdateResponse response     = new UserFormCreateUpdateResponse();
            List<ProfilesDTO>            profilesDTOS = this.profilesDI.find(getConnection(), null);
            
            response.setProfiles(GsonUtil.convertObjectListTo(profilesDTOS, ProfileResponse[].class));
            
            if (request != null) {
                UsersDTO userDTO = getUser(request);
                response.setUser(convert(userDTO));
                response.getUser()
                        .setProfile(GsonUtil.convertObjectTo(userDTO.getProfilesDTO(), Profile.class));
            }
            
            return response;
        } finally {
            closeConnection();
        }
    }
    
    private UsersDTO getUser(Integer id) throws Exception {
        return this.usersDI.findById(getConnection(), id);
    }
    
    private UserResponse convert(UsersDTO dto) {
        return GsonUtil.convertObjectTo(dto, UserResponse.class);
    }
}
