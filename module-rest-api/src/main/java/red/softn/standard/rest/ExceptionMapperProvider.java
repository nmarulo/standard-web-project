package red.softn.standard.rest;

import red.softn.standard.common.rest.CustomHttpException;

import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapperProvider implements ExceptionMapper<Exception> {
    
    @Override
    public Response toResponse(Exception ex) {
        Response.Status     status              = Response.Status.INTERNAL_SERVER_ERROR;
        String              message             = "Algo salió mal. Inténtalo mas tarde.";
        CustomHttpException customHttpException = getCustomHttpException(ex);
        
        if (customHttpException != null) {
            status = customHttpException.getStatus();
            message = customHttpException.getMessage();
        }
        
        return Response.status(status)
                       .header("exception", message)
                       .build();
    }
    
    private boolean isCustomHttpException(Exception ex) {
        return ex instanceof EJBException && ((EJBException) ex).getCausedByException() instanceof CustomHttpException;
    }
    
    private CustomHttpException getCustomHttpException(Exception ex) {
        if (isCustomHttpException(ex)) {
            return (CustomHttpException) ((EJBException) ex).getCausedByException();
        }
        
        return null;
    }
}
