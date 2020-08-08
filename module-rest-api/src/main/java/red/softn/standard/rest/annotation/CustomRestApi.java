package red.softn.standard.rest.annotation;

import javax.ws.rs.core.Response;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomRestApi {
    
    Response.Status statusResponse() default Response.Status.OK;
    
    boolean canNullRequest() default false;
}
