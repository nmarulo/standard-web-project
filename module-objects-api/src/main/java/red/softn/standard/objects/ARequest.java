package red.softn.standard.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ARequest<R> {
    
    private R request;
    
    private String token;
    
    private String userName;
    
    public ARequest() {}
    
    public ARequest(R request) {
        this.request = request;
    }
    
}
