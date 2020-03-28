package red.softn.standard.objects.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import red.softn.standard.objects.MultiDateDeserializer;

import java.util.Date;

@Setter
@Getter
public class Users {
    
    private Integer id;
    
    private String userLogin;
    
    private String userName;
    
    private String userEmail;
    
    private String userPassword;
    
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private Date userRegistered;
    
    private Integer profileId;
    
    private String userUrlImage;
}
