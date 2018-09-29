package red.softn.standard.objects.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserResponse {
    
    private long id;
    
    private String userLogin;
    
    private String userName;
    
    private String userEmail;
    
    private String userPassword;
    
    private Date userRegistered;
}
