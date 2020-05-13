package red.softn.standard.jsf.bean;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import red.softn.standard.common.GsonUtil;
import red.softn.standard.jsf.common.FacesUtils;
import red.softn.standard.jsf.rest.UsersRC;
import red.softn.standard.objects.request.UserRequest;
import red.softn.standard.objects.response.UserResponse;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "userBean")
@ViewScoped
public class UsersBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Getter
    @Setter
    private UserRequest request;
    
    @Getter
    private List<UserResponse> users;
    
    private final UsersRC usersRC;
    
    public UsersBean() {
        this.usersRC = new UsersRC();
        this.request = selectById(FacesUtils.getRequestParameter("id"));
    }
    
    public void deleteSelectedUser(UserResponse user) {
        if (this.usersRC.deleteById(user.getId())) {
            System.out.println("El usuario a sido borrado correctamente.");
            this.users.remove(user);
        }
    }
    
    public void createUpdateUser() {
        if (isCreatingRecord()) {
            this.request = convert(this.usersRC.insert(this.request));
            
            if (this.request.getId() != null) {
                FacesUtils.redirectPram("form.xhtml", "id", this.request.getId()
                                                                        .toString());
            }
        } else {
            this.request = convert(this.usersRC.update(this.request));
        }
    }
    
    public void searchAll() {
        this.users = this.usersRC.getAll(this.request);
    }
    
    public void resetSearchForm() {
        this.request = new UserRequest();
    }
    
    public String getActionForm() {
        if (isCreatingRecord()) {
            return "Crear";
        }
        
        return "Modificar";
    }
    
    public boolean isCreatingRecord() {
        return this.request.getId() == null;
    }
    
    public void updatePassword() {
    
    }
    
    private UserRequest selectById(String id) {
        if (StringUtils.isBlank(id)) {
            return new UserRequest();
        }
        
        return convert(this.usersRC.getById(id));
    }
    
    private UserRequest convert(UserResponse response) {
        return GsonUtil.convertObjectTo(response, UserRequest.class);
    }
}
