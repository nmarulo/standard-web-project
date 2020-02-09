package red.softn.standard.rest;

import red.softn.standard.objects.ARequest;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

public abstract class ARestService {
    
    @HeaderParam("Authentication")
    private String token;
    
    protected String getUserName() {
        if (this.token == null) {
            return null;
        }
        
        return token;
    }
    
    /**
     * Método que encapsula la referencia del método a ejecutar.
     * <p>
     * La respuesta se retorna, por defecto, con el estado 200.
     * <p>
     * Si el cuerpo de la petición esta vacía, la respuesta se retorna con el estado 400.
     *
     * @param body   Cuerpo de la petición.
     * @param method Método a ejecutar.
     * @param <B>    Tipo del cuerpo de la petición.
     * @param <R>    Tipo del resultado de la consulta.
     *
     * @return Response
     */
    protected <B, R> Response getResponse(B body, ThrowingFunction<R> method) {
        return getResponse(body, method, Response.Status.OK);
    }
    
    /**
     * Método que encapsula la referencia del método (void) a ejecutar.
     * <p>
     * La respuesta se retorna, por defecto, con el estado 200.
     * <p>
     * Si el cuerpo de la petición esta vacía, la respuesta se retorna con el estado 400.
     *
     * @param body   Cuerpo de la petición.
     * @param method Método a ejecutar.
     * @param <B>    Tipo del cuerpo de la petición.
     *
     * @return Response
     */
    protected <B> Response getResponse(B body, ThrowingConsumer method) {
        return getResponse(body, method, Response.Status.OK);
    }
    
    /**
     * Método que encapsula la referencia del método (void) a ejecutar.
     * <p>
     * La respuesta se retorna, por defecto, con el estado 200.
     * <p>
     * Si el cuerpo de la petición esta vacía, la respuesta se retorna con el estado 400.
     *
     * @param body   Cuerpo de la petición.
     * @param method Método a ejecutar.
     * @param status Estado de la respuesta.
     * @param <B>    Tipo del cuerpo de la petición.
     *
     * @return Response
     */
    protected <B> Response getResponse(B body, ThrowingConsumer method, Response.Status status) {
        return getResponse(body, method, status, false);
    }
    
    /**
     * Método que encapsula la referencia del método (void) a ejecutar.
     * <p>
     * La respuesta se retorna, por defecto, con el estado 200.
     * <p>
     * Si el cuerpo de la petición esta vacía, la respuesta se retorna con el estado 400.
     *
     * @param body        Cuerpo de la petición.
     * @param method      Método a ejecutar.
     * @param bodyCanNull Establece si el cuerpo de la petición no puede ser nulo (true) o si (false)
     * @param <B>         Tipo del cuerpo de la petición.
     *
     * @return Response
     */
    protected <B> Response getResponse(B body, ThrowingConsumer method, boolean bodyCanNull) {
        return getResponse(body, method, Response.Status.OK, bodyCanNull);
    }
    
    /**
     * Método que encapsula la referencia del método a ejecutar.
     * <p>
     * La respuesta se retorna, por defecto, con el estado 200.
     * <p>
     * Si el cuerpo de la petición esta vacía, la respuesta se retorna con el estado 400.
     *
     * @param body        Cuerpo de la petición.
     * @param method      Método a ejecutar.
     * @param status      Estado de la respuesta.
     * @param bodyCanNull Establece si el cuerpo de la petición no puede ser nulo (true) o si (false)
     * @param <B>         Tipo del cuerpo de la petición.
     *
     * @return Response
     */
    protected <B> Response getResponse(B body, ThrowingConsumer method, Response.Status status, boolean bodyCanNull) {
        return getResponse(() -> {
            checkBody(body, bodyCanNull);
            method.accept(fillParams(body));
        }, status);
    }
    
    /**
     * Método que encapsula la referencia del método a ejecutar.
     * <p>
     * La respuesta se retorna, por defecto, con el estado 200.
     * <p>
     * Si el cuerpo de la petición esta vacía, la respuesta se retorna con el estado 400.
     *
     * @param method Método a ejecutar.
     *
     * @return Response
     */
    protected Response getResponse(ThrowingRunnable method) {
        return getResponse(method, Response.Status.OK);
    }
    
    /**
     * Método que encapsula la referencia del método a ejecutar.
     * <p>
     * La respuesta se retorna, por defecto, con el estado 200.
     * <p>
     * Si el cuerpo de la petición esta vacía, la respuesta se retorna con el estado 400.
     *
     * @param method Método a ejecutar.
     * @param status Estado de la respuesta.
     *
     * @return Response
     */
    protected Response getResponse(ThrowingRunnable method, Response.Status status) {
        return getResponse(() -> {
            method.run();
            
            return Response.status(status)
                           .build();
        });
    }
    
    /**
     * Método que encapsula la referencia del método a ejecutar.
     * <p>
     * La respuesta se retorna, por defecto, con el estado 200.
     * <p>
     * Si el cuerpo de la petición esta vacía, la respuesta se retorna con el estado 400.
     *
     * @param body        Cuerpo de la petición.
     * @param method      Método a ejecutar.
     * @param bodyCanNull Establece si el cuerpo de la petición no puede ser nulo (true) o si (false)
     * @param <B>         Tipo del cuerpo de la petición.
     * @param <R>         Tipo del resultado de la consulta.
     *
     * @return Response
     */
    protected <B, R> Response getResponse(B body, ThrowingFunction<R> method, boolean bodyCanNull) {
        return getResponse(body, method, Response.Status.OK, bodyCanNull);
    }
    
    /**
     * Método que encapsula la referencia del método a ejecutar.
     * <p>
     * La respuesta se retorna, por defecto, con el estado 200.
     *
     * @param method Método a ejecutar, sin parámetros.
     * @param <R>    Tipo del resultado de la consulta.
     *
     * @return Response
     */
    protected <R> Response getResponse(ThrowingSupplier<R> method) {
        return getResponse(method, Response.Status.OK);
    }
    
    /**
     * Método que encapsula la referencia del método a ejecutar.
     * <p>
     * La respuesta se retorna, por defecto, con el estado 200.
     *
     * @param method Método a ejecutar, sin parámetros.
     * @param status Estado de la respuesta.
     * @param <R>    Tipo del resultado de la consulta.
     *
     * @return Response
     */
    protected <R> Response getResponse(ThrowingSupplier<R> method, Response.Status status) {
        return getResponse(null, (value) -> { return method.get();}, status, true);
    }
    
    /**
     * Método que encapsula la referencia del método a ejecutar.
     * <p>
     * La respuesta se retorna, por defecto, con el estado 200.
     * <p>
     * Si el cuerpo de la petición esta vacía, la respuesta se retorna con el estado 400.
     *
     * @param body   Cuerpo de la petición.
     * @param method Método a ejecutar.
     * @param status Estado de la respuesta.
     * @param <B>    Tipo del cuerpo de la petición.
     * @param <R>    Tipo del resultado de la consulta.
     *
     * @return Response
     */
    protected <B, R> Response getResponse(B body, ThrowingFunction<R> method, Response.Status status) {
        return getResponse(body, method, status, false);
    }
    
    /**
     * Método que encapsula la referencia del método a ejecutar.
     * <p>
     * Si el cuerpo de la petición esta vacía, la respuesta se retorna con el estado 400.
     *
     * @param body        Cuerpo de la petición.
     * @param method      Método a ejecutar.
     * @param status      Estado de la respuesta.
     * @param bodyCanNull Establece si el cuerpo de la petición no puede ser nulo (true) o si (false)
     * @param <B>         Tipo del cuerpo de la petición.
     * @param <R>         Tipo del resultado de la consulta.
     *
     * @return Response
     */
    protected <B, R> Response getResponse(B body, ThrowingFunction<R> method, Response.Status status, boolean bodyCanNull) {
        return getResponse(() -> {
            checkBody(body, bodyCanNull);
            
            Response response;
            R        entity = method.apply(fillParams(body));
            
            if (entity instanceof Response) {
                response = (Response) entity;
            } else {
                response = Response.status(status)
                                   .entity(entity)
                                   .build();
            }
            
            return response;
        });
    }
    
    private <B> void checkBody(B body, boolean bodyCanNull) throws Exception {
        if (body == null && !bodyCanNull) {
            throw new Exception("El cuerpo de la petición no puede estar vació.");
        }
    }
    
    private Response getResponse(ThrowingSupplierResponse throwingSupplier) {
        Response response;
        try {
            response = throwingSupplier.get();
        } catch (Exception ex) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                               .header("exception", ex.getMessage())
                               .build();
        }
        
        return response;
    }
    
    
    protected <R> ARequest fillParams(R request) throws Exception {
        // Aquí podemos añadir todos los parámetros que necesitemos en el backend
        ARequest<R> newRequest = new ARequest<>(request);
        newRequest.setToken(getToken());
        newRequest.setUserName(getUserName());
        
        return newRequest;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    @FunctionalInterface
    public interface ThrowingFunction<R> {
        
        R apply(ARequest arg) throws Exception;
    }
    
    @FunctionalInterface
    public interface ThrowingSupplier<R> {
        
        R get() throws Exception;
    }
    
    @FunctionalInterface
    public interface ThrowingConsumer {
        
        void accept(ARequest arg) throws Exception;
    }
    
    @FunctionalInterface
    public interface ThrowingRunnable {
        
        void run() throws Exception;
    }
    
    @FunctionalInterface
    public interface ThrowingSupplierResponse {
        
        Response get() throws Exception;
    }
    
}
