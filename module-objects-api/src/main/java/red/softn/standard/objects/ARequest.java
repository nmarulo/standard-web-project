package red.softn.standard.objects;

public class ARequest<R> {
    
    private R request;
    
    private String token;
    
    private String userName;
    
    public ARequest() {}
    
    public ARequest(R request) {
        this.request = request;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public R getRequest() {
        return request;
    }
    
    public void setRequest(R request) {
        this.request = request;
    }
}
