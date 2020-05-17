package red.softn.standard.jsf.bean;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import red.softn.standard.common.GsonUtil;
import red.softn.standard.jsf.common.FacesUtils;
import red.softn.standard.jsf.rest.UsersRC;
import red.softn.standard.objects.request.UserRequest;
import red.softn.standard.objects.response.ProfileResponse;
import red.softn.standard.objects.response.UserFormCreateUpdateResponse;
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
    
    @Getter
    private List<ProfileResponse> profiles;
    
    public UsersBean() {
        this.usersRC = new UsersRC();
        formCreateUpdate(FacesUtils.getRequestParameter("id"));
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
    
    private void formCreateUpdate(String id) {
        UserFormCreateUpdateResponse form = this.usersRC.formCreateUpdate(id);
        
        this.profiles = form.getProfiles();
        this.request = convert(form.getUser());
        
        if (this.request == null) {
            this.request = new UserRequest();
        }
    }
    
    private UserRequest convert(UserResponse response) {
        return GsonUtil.convertObjectTo(response, UserRequest.class);
    }
}
