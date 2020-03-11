package red.softn.standard.db.dto;

import lombok.Data;
import lombok.ToString;
import red.softn.standard.common.gson.GsonExclude;

import javax.persistence.*;

@Entity
@Table(name = "profiles_permissions")
@Data
public class ProfilesPermissionsDTO {
    
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "profileId", column = @Column(name = "profile_id")),
        @AttributeOverride(name = "permissionId", column = @Column(name = "permission_id"))
                        })
    private ProfilesPermissionsDTOPK profilesPermissionsDTOPK;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id", insertable = false, updatable = false)
    @GsonExclude
    @ToString.Exclude
    private ProfilesDTO profilesDTO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", referencedColumnName = "id", insertable = false, updatable = false)
    @GsonExclude
    @ToString.Exclude
    private PermissionsDTO permissionsDTO;
    
}
