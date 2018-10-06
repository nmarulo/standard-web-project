package red.softn.standard.jsf.bean;

import lombok.Getter;
import lombok.Setter;
import red.softn.standard.objects.request.UserRequest;
import red.softn.standard.objects.response.UserResponse;

import javax.faces.event.ActionListener;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Named(value = "userBean")
@ViewScoped
public class UsersBean implements Serializable {
    
    @Getter
    @Setter
    private UserResponse userResponse;
    
    @Getter
    private List<UserResponse> userResponseList;
    
    public UsersBean() {
        this.userResponse = new UserResponse() {{
            setId(1);
            setUserEmail("test@softn.red");
            setUserLogin("test");
            setUserName("test");
            setUserRegistered(new Date());
            setUserPassword("123");
        }};
        this.userResponseList = Arrays.asList(new UserResponse() {{
            setId(1);
            setUserEmail("test@softn.red");
            setUserLogin("test");
            setUserName("test");
            setUserRegistered(new Date());
            setUserPassword("123");
        }}, new UserResponse() {{
            setId(2);
            setUserEmail("test2@softn.red");
            setUserLogin("test2");
            setUserName("test2");
            setUserRegistered(new Date());
            setUserPassword("123");
        }}, new UserResponse() {{
            setId(3);
            setUserEmail("test3@softn.red");
            setUserLogin("test3");
            setUserName("test3");
            setUserRegistered(new Date());
            setUserPassword("123");
        }});
    }
    
    public void deleteSelectedUser() {
    }
    
    public void createUpdateUser() {
        //Si el id es nulo, se envía la petición de crear usuario
        //y si no es nulo, se envía la petición de actualizar usuario.
    }
}
