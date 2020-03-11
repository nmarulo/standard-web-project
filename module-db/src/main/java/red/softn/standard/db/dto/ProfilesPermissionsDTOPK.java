package red.softn.standard.db.dto;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class ProfilesPermissionsDTOPK implements Serializable {
    
    private Integer profileId;
    
    private Integer permissionId;
    
}
