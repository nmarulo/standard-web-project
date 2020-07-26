package red.softn.standard.common.rest;

import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.core.Response;

public class CustomHttpException extends RuntimeException {
    
    @Getter
    @Setter
    private Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
    
    public CustomHttpException() {
        this("Algo salió mal. Inténtalo mas tarde.");
    }
    
    public CustomHttpException(String message) {
        super(message);
    }
    
    public CustomHttpException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CustomHttpException(Throwable cause) {
        super(cause);
    }
    
    public CustomHttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
