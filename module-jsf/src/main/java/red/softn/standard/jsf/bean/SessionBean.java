package red.softn.standard.jsf.bean;

import lombok.Getter;
import red.softn.standard.objects.response.UserResponse;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named(value = "sessionBean")
public class SessionBean implements Serializable {
    
    public static final String USER_NAME = "username";
    
    @Getter
    private UserResponse userResponse;
    
    public SessionBean() {
        this.userResponse = new UserResponse();
    }
}
