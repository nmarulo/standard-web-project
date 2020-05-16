package red.softn.standard.objects.response;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class UserFormCreateUpdateResponse {
    
    private UserResponse user;
    
    private List<ProfileResponse> profiles;
    
    public UserFormCreateUpdateResponse() {
        this.profiles = new LinkedList<>();
    }
}
