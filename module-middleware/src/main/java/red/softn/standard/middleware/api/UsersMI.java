package red.softn.standard.middleware.api;

import red.softn.standard.objects.ARequest;
import red.softn.standard.objects.request.UserRequest;
import red.softn.standard.objects.response.UserResponse;

import java.util.List;

public interface UsersMI {
    
    List<UserResponse> get(ARequest<UserRequest> request) throws Exception;
    
    UserResponse getById(ARequest<Integer> request) throws Exception;
    
    UserResponse post(ARequest<UserRequest> request) throws Exception;
    
    UserResponse put(ARequest<UserRequest> request) throws Exception;
    
    void deleteById(ARequest<Integer> request) throws Exception;
}
