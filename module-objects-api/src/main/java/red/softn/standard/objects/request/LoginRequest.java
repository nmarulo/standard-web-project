package red.softn.standard.objects.request;

import lombok.Getter;
import lombok.Setter;
import red.softn.standard.objects.pojo.User;

@Setter
@Getter
public class LoginRequest extends User {
    
    private boolean rememberMe;
}
