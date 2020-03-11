package red.softn.standard.db.dto;

import lombok.Data;
import lombok.ToString;
import red.softn.standard.common.gson.GsonExclude;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "permissions")
@Data
public class PermissionsDTO {
    
    @Id
    @Column(name = "id")
    private Integer id;
    
    @Basic
    @Column(name = "permission_name")
    private String permissionName;
    
    @Basic
    @Column(name = "permission_description")
    private String permissionDescription;
    
    @Basic
    @Column(name = "permission_key_name")
    private String permissionKeyName;
    
    @OneToMany(mappedBy = "permissionsDTO", fetch = FetchType.LAZY)
    @GsonExclude
    @ToString.Exclude
    private List<ProfilesPermissionsDTO> profilesPermissionsDTO;
    
}
