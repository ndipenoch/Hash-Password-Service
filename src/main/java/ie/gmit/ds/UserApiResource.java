package ie.gmit.ds;

// Adapted from https://howtodoinjava.com/dropwizard/tutorial-and-hello-world-example/

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/users")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserApiResource {

    private final Validator validator;

    public UserApiResource(Validator validator) {
        this.validator = validator;
    }

    @GET
    public Response getUsers() {
        return Response.ok(UserDB.getUsers()).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Integer id) {
        User user = UserDB.getUser(id);
        if (user != null) {
            return Response.ok(user).build();
        }
        else{
            return Response.status(Status.NOT_FOUND).entity(id + " does not exsit.").build();
        }

    }

}
