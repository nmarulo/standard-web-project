package red.softn.standard.rest;

import red.softn.standard.common.rest.CustomHttpException;

import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapperProvider implements ExceptionMapper<Exception> {
    
    public static final String EXCEPTION_HEADER_NAME = "exception";
    
    @Override
    public Response toResponse(Exception ex) {
        CustomHttpException customHttpException = getCustomHttpException(ex);
        
        return Response.status(customHttpException.getStatus())
                       .header(EXCEPTION_HEADER_NAME, customHttpException.getMessage())
                       .build();
    }
    
    private boolean isEjbCustomHttpException(Exception ex) {
        return ex instanceof EJBException && ((EJBException) ex).getCausedByException() instanceof CustomHttpException;
    }
    
    private CustomHttpException getCustomHttpException(Exception ex) {
        CustomHttpException customHttpException;
        
        if (isEjbCustomHttpException(ex)) {
            customHttpException = (CustomHttpException) ((EJBException) ex).getCausedByException();
        } else if (ex instanceof CustomHttpException) {
            customHttpException = (CustomHttpException) ex;
        } else {
            customHttpException = new CustomHttpException();
        }
        
        return customHttpException;
    }
}
