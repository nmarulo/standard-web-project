package red.softn.standard.jsf.rest;

import red.softn.standard.common.AHttpClient;
import red.softn.standard.objects.request.UserRequest;
import red.softn.standard.objects.response.UserResponse;

import java.util.LinkedList;
import java.util.List;

public class UsersRC extends AHttpClient {
    
    public UsersRC() {
        super("http://localhost:8888/module-rest-api/api", "users");
    }
    
    @Override
    protected void actionResponseException() {
    
    }
    
    public List<UserResponse> getAll(UserRequest request) {
        try {
            return get(request, UserResponse[].class);
        } catch (Exception ex) {
            System.err.println(ex);
        }
        
        return new LinkedList<>();
    }
    
    public UserResponse getById(String id) {
        try {
            return get(UserResponse.class, id);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    
        return new UserResponse();
    }
    
    public UserResponse insert(UserRequest request) {
        try {
            return post(request, UserResponse.class);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    
        return new UserResponse();
    }
    
    public UserResponse update(UserRequest request) {
        try {
            return put(request, UserResponse.class);
        } catch (Exception ex) {
            System.err.println(ex);
        }
        
        return new UserResponse();
    }
    
    public boolean remove(UserRequest request) {
        try {
            delete(request);
            
            return true;
        } catch (Exception ex) {
            System.err.println(ex);
        }
        
        return false;
    }
    
    public boolean removeById(int id) {
        try {
            delete(String.valueOf(id));
            
            return true;
        } catch (Exception ex) {
            System.err.println(ex);
        }
        
        return false;
    }
}
