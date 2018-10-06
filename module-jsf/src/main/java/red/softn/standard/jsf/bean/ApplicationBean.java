package red.softn.standard.jsf.bean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;

@ApplicationScoped
@Named(value = "applicationBean")
public class ApplicationBean implements Serializable {
    
    public String version() {
        return "0.1.0-ALPHA";
    }
    
    public int getSessionTimeoutValue() {
        return 30;
    }
}
