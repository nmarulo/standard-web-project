package red.softn.standard.middleware.ejb;

import red.softn.standard.common.GsonUtil;
import red.softn.standard.db.api.UsersDI;
import red.softn.standard.db.dto.UsersDTO;
import red.softn.standard.middleware.MiddlewareEJB;
import red.softn.standard.middleware.api.UsersMI;
import red.softn.standard.objects.ARequest;
import red.softn.standard.objects.request.UserRequest;
import red.softn.standard.objects.response.UserResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class UsersM extends MiddlewareEJB implements UsersMI {
    
    @Inject
    private UsersDI usersDI;
    
    @Override
    public List<UserResponse> get(ARequest<UserRequest> aRequest) throws Exception {
        try {
            UserRequest    request = aRequest.getRequest();
            List<UsersDTO> dtoList = this.usersDI.find(getConnection(), GsonUtil.convertObjectTo(request, UsersDTO.class));
    
            return GsonUtil.convertObjectListTo(dtoList, UserResponse[].class);
        }finally {
            closeConnection();
        }
    }
    
    @Override
    public UserResponse getById(ARequest<Integer> aRequest) throws Exception {
        try {
            UsersDTO dto = this.usersDI.findById(getConnection(), aRequest.getRequest());
    
            return GsonUtil.convertObjectTo(dto, UserResponse.class);
        }finally {
            closeConnection();
        }
    }
    
    @Override
    public UserResponse post(ARequest<UserRequest> aRequest) throws Exception {
        try {
            UserRequest request = aRequest.getRequest();
            UsersDTO    dto     = this.usersDI.insert(getConnection(), GsonUtil.convertObjectTo(request, UsersDTO.class));
    
            return GsonUtil.convertObjectTo(dto, UserResponse.class);
        }finally {
            closeConnection();
        }
    }
    
    @Override
    public UserResponse put(ARequest<UserRequest> aRequest) throws Exception {
        try {
            UserRequest request = aRequest.getRequest();
            UsersDTO    dto     = this.usersDI.update(getConnection(), GsonUtil.convertObjectTo(request, UsersDTO.class));
    
            return GsonUtil.convertObjectTo(dto, UserResponse.class);
        }finally {
            closeConnection();
        }
    }
    
    @Override
    public void delete(ARequest<UserRequest> aRequest) throws Exception {
        try {
            UserRequest request = aRequest.getRequest();
            this.usersDI.delete(getConnection(), GsonUtil.convertObjectTo(request, UsersDTO.class));
        }finally {
            closeConnection();
        }
    }
    
    @Override
    public void deleteById(ARequest<Integer> aRequest) throws Exception {
        try {
            this.usersDI.deleteById(getConnection(), aRequest.getRequest());
        }finally {
            closeConnection();
        }
    }
}
