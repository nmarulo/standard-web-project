package red.softn.standard.db.dto;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class UsersDTO {
    
    @Id
    @Column(name = "id")
    private Integer id;
    
    @Basic
    @Column(name = "user_login")
    private String userLogin;
    
    @Basic
    @Column(name = "user_name")
    private String userName;
    
    @Basic
    @Column(name = "user_email")
    private String userEmail;
    
    @Basic
    @Column(name = "user_password")
    private String userPassword;
    
    @Basic
    @Column(name = "user_registered")
    @Temporal(value = TemporalType.DATE)
    private Date userRegistered;
    
    @Basic
    @Column(name = "user_url_image")
    private String userUrlImage;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    @ToString.Exclude
    private ProfilesDTO profilesDTO;
    
}
