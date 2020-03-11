package red.softn.standard.db.dto;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "settings")
@Data
public class SettingsDTO {
    
    @Id
    @Column(name = "id")
    private Integer id;
    
    @Basic
    @Column(name = "setting_name")
    private String  settingName;
    
    @Basic
    @Column(name = "setting_value")
    private String  settingValue;
    
    @Basic
    @Column(name = "setting_description")
    private String  settingDescription;
    
}
