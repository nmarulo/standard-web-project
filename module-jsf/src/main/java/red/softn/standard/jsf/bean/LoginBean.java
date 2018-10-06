package red.softn.standard.jsf.bean;

import lombok.Getter;
import lombok.Setter;
import red.softn.standard.objects.response.UserResponse;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@ViewScoped
@Named(value = "loginBean")
public class LoginBean implements Serializable {
    
    @Getter
    @Setter
    private UserResponse userResponse;
    
    public LoginBean() {
        this.userResponse = new UserResponse();
    }
    
    public void login() {
    
    }
    
    public void logout() {
    
    }
}
