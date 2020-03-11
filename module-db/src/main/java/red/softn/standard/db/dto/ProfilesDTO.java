package red.softn.standard.db.dto;

import lombok.Data;
import lombok.ToString;
import red.softn.standard.common.gson.GsonExclude;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "profiles")
@Data
public class ProfilesDTO {
    
    @Id
    @Column(name = "id")
    private Integer id;
    
    @Basic
    @Column(name = "profile_name")
    private String profileName;
    
    @Basic
    @Column(name = "profile_description")
    private String profileDescription;
    
    @Basic
    @Column(name = "profile_key_name")
    private String profileKeyName;
    
    @OneToMany(mappedBy = "profilesDTO", fetch = FetchType.LAZY)
    @GsonExclude
    @ToString.Exclude
    private List<ProfilesPermissionsDTO> profilesPermissionsDTO;
    
    @OneToMany(mappedBy = "profilesDTO", fetch = FetchType.LAZY)
    @GsonExclude
    @ToString.Exclude
    private List<UsersDTO> usersDTO;
    
}
