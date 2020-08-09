package red.softn.standard.rest;

import javax.ejb.EJBException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapperProvider implements ExceptionMapper<Exception> {
    
    @Override
    public Response toResponse(Exception ex) {
        WebApplicationException customHttpException = getCustomHttpException(ex);
        Response                response            = customHttpException.getResponse();
        
        return Response.status(response.getStatusInfo())
                       .entity(customHttpException.getMessage())
                       .type(response.getMediaType())
                       .build();
    }
    
    private boolean isEJBException(Exception ex) {
        return ex instanceof EJBException && ((EJBException) ex).getCausedByException() instanceof WebApplicationException;
    }
    
    private WebApplicationException getCustomHttpException(Exception ex) {
        WebApplicationException customHttpException;
        
        if (isEJBException(ex)) {
            customHttpException = (WebApplicationException) ((EJBException) ex).getCausedByException();
        } else if (ex instanceof WebApplicationException) {
            customHttpException = (WebApplicationException) ex;
        } else {
            System.err.println("Error no controlado.");
            ex.printStackTrace();
            customHttpException = new WebApplicationException("Algo salió mal. Inténtalo mas tarde.");
        }
        
        return customHttpException;
    }
}
