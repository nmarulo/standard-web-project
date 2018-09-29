package red.softn.standard.objects.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class UserRequest {
    
    private long id;
    
    private String userLogin;
    
    private String userName;
    
    private String userEmail;
    
    private String userPassword;
    
    private Date userRegistered;
}
