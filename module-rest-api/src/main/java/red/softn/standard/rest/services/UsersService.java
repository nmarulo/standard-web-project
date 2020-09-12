package red.softn.standard.rest.services;

import red.softn.standard.middleware.api.UsersMI;
import red.softn.standard.objects.request.UserRequest;
import red.softn.standard.objects.response.UserFormCreateUpdateResponse;
import red.softn.standard.objects.response.UserResponse;
import red.softn.standard.rest.annotation.CustomRestApi;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersService {
    
    @Inject
    private UsersMI usersMI;
    
    @GET
    public List<UserResponse> search(@BeanParam UserRequest request) throws Exception {
        return this.usersMI.search(request);
    }
    
    @GET
    @Path("{id}")
    public UserResponse getById(@PathParam("id") Integer request) throws Exception {
        return this.usersMI.getById(request);
    }
    
    @POST
    @CustomRestApi(statusResponse = Response.Status.CREATED)
    public UserResponse post(UserRequest request) throws Exception {
        return this.usersMI.post(request);
    }
    
    @PUT
    public UserResponse put(UserRequest request) throws Exception {
        return this.usersMI.put(request);
    }
    
    @DELETE
    @Path("{id}")
    @CustomRestApi(statusResponse = Response.Status.NO_CONTENT)
    public void deleteById(@PathParam("id") Integer request) throws Exception {
        this.usersMI.deleteById(request);
    }
    
    @GET
    @Path("form-create-update")
    public UserFormCreateUpdateResponse formCreateUpdate() throws Exception {
        return formCreateUpdate(null);
    }
    
    @GET
    @Path("form-create-update/{id}")
    public UserFormCreateUpdateResponse formCreateUpdate(@PathParam("id") Integer request) throws Exception {
        return this.usersMI.formCreateUpdate(request);
    }
}
