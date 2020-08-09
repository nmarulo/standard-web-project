package red.softn.standard.objects.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import red.softn.standard.objects.annotation.DateFormatterParamConverter;
import red.softn.standard.objects.MultiDateDeserializer;

import javax.ws.rs.QueryParam;
import java.util.Date;

@Setter
@Getter
public class User {
    
    @QueryParam("id")
    private Integer id;
    
    @QueryParam("userLogin")
    private String userLogin;
    
    @QueryParam("userName")
    private String userName;
    
    @QueryParam("userEmail")
    private String userEmail;
    
    @QueryParam("userPassword")
    private String userPassword;
    
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @QueryParam("userRegistered")
    @DateFormatterParamConverter
    private Date userRegistered;
    
    @QueryParam("profileId")
    private Integer profileId;
    
    @QueryParam("userUrlImage")
    private String userUrlImage;
    
    private Profile profile;
}
