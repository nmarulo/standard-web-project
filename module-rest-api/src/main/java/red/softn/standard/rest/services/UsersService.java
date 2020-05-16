package red.softn.standard.rest.services;

import red.softn.standard.middleware.api.UsersMI;
import red.softn.standard.objects.request.UserRequest;
import red.softn.standard.rest.ARestService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersService extends ARestService {
    
    @Inject
    private UsersMI usersMI;
    
    @GET
    public Response get(@BeanParam UserRequest request) {
        return getResponse(request, this.usersMI::get, true);
    }
    
    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") Integer id) {
        return getResponse(id, this.usersMI::getById);
    }
    
    @POST
    public Response post(UserRequest request) {
        return getResponse(request, this.usersMI::post);
    }
    
    @PUT
    public Response put(UserRequest request) {
        return getResponse(request, this.usersMI::put);
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") Integer id) {
        return getResponse(id, this.usersMI::deleteById);
    }
    
    @GET
    @Path("form-create-update")
    public Response formCreateUpdate() {
        return formCreateUpdate(null);
    }
    
    @GET
    @Path("form-create-update/{id}")
    public Response formCreateUpdate(@PathParam("id") Integer id) {
        return getResponse(id, this.usersMI::formCreateUpdate, true);
    }
}
