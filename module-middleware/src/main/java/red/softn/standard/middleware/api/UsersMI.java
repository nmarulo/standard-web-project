package red.softn.standard.middleware.api;

import red.softn.standard.objects.request.UserRequest;
import red.softn.standard.objects.response.UserFormCreateUpdateResponse;
import red.softn.standard.objects.response.UserResponse;

import java.util.List;

public interface UsersMI {
    
    List<UserResponse> search(UserRequest request) throws Exception;
    
    UserResponse getById(Integer request) throws Exception;
    
    UserResponse post(UserRequest request) throws Exception;
    
    UserResponse put(UserRequest request) throws Exception;
    
    void deleteById(Integer request) throws Exception;
    
    UserFormCreateUpdateResponse formCreateUpdate(Integer request) throws Exception;
}
