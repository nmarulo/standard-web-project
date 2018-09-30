package red.softn.standard.rest.services;

import red.softn.standard.objects.response.UserResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersService {
    
    @GET
    public Response getUsers() {
        return Response.ok()
                       .entity(new UserResponse() {{
                           setId(1);
                           setUserEmail("test@softn.red");
                           setUserLogin("test");
                           setUserName("test");
                           setUserRegistered(new Date());
                           setUserPassword("123");
                       }})
                       .build();
    }
}
