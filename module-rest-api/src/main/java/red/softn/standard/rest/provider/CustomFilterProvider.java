package red.softn.standard.rest.provider;

import org.apache.commons.lang3.StringUtils;
import red.softn.standard.common.rest.CustomHttpException;
import red.softn.standard.rest.ExceptionMapperProvider;
import red.softn.standard.rest.annotation.CustomRestApi;

import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

@Provider
public class CustomFilterProvider implements ContainerRequestFilter, ContainerResponseFilter {
    
    @Context
    private ResourceInfo resourceInfo;
    
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        checkAnnotations(CustomRestApi::canNullRequest, value -> this.checkBody(value, containerRequestContext));
    }
    
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        String headerString = containerResponseContext.getHeaderString(ExceptionMapperProvider.EXCEPTION_HEADER_NAME);
        
        if (StringUtils.isBlank(headerString)) {
            checkAnnotations(CustomRestApi::statusResponse, containerResponseContext::setStatusInfo);
        }
    }
    
    private <R> void checkAnnotations(Function<CustomRestApi, R> mapFunction, Consumer<R> consumer) {
        consumer.accept(mapFunction.apply(getCustomRestApi()));
    }
    
    private void checkBody(boolean canNullRequest, ContainerRequestContext containerRequestContext) {
        if (containerRequestContext.getLength() <= 0 && !canNullRequest) {
            throw new CustomHttpException("El cuerpo de la petición no puede estar vació.", Response.Status.BAD_REQUEST);
        }
    }
    
    private CustomRestApi getCustomRestApi() {
        CustomRestApi annotation = resourceInfo.getResourceMethod()
                                               .getAnnotation(CustomRestApi.class);
        
        if (annotation == null) {
            @CustomRestApi
            final class CustomRestApiImpl {}
            annotation = CustomRestApiImpl.class.getAnnotation(CustomRestApi.class);
        }
        
        return annotation;
    }
    
}
