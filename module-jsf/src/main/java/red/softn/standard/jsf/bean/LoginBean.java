package red.softn.standard.jsf.bean;

import lombok.Getter;
import lombok.Setter;
import red.softn.standard.jsf.common.FacesUtils;
import red.softn.standard.objects.request.LoginRequest;
import red.softn.standard.objects.response.UserResponse;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@ViewScoped
@Named(value = "loginBean")
public class LoginBean implements Serializable {
    
    @Getter
    @Setter
    private UserResponse userResponse;
    
    @Getter
    @Setter
    private LoginRequest loginRequest;
    
    public LoginBean() {
        this.loginRequest = new LoginRequest();
    }
    
    public void login() {
//        SessionBean.getInstance()
//                   .setUser(user);
        FacesContext fCtx = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fCtx.getExternalContext()
                                                .getSession(false);
//        SessionBean.getInstance()
//                   .setIdSession(session.getId());
    
        session.setAttribute(SessionBean.USER_NAME, "nicolas");
        FacesUtils.redirectTo("dashboard/index");
    }
    
    public void logout() {
    
    }
}
