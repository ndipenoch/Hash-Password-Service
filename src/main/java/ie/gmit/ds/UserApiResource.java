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

    @POST
    public Response updateUser(User user) throws URISyntaxException {
        // validation
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        User u = UserDB.getUser(user.getuID());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<User> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        if (u != null) {
            UserDB.updateUser(user.getuID(),user);
            return Response.ok("User id "+user.getuID()+" is sucessfully updated")
                    .build();
        }else
            return Response.status(Status.NOT_FOUND).entity("Can not update. User id "+user.getuID()+" does not exist").build();
    }

    @PUT
    @Path("/{id}")
    public Response createUserById(@PathParam("id") Integer id, User user) {
        // validation
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if(user.getuID().equals(id)){
            //Get user from the database
            User u = UserDB.getUser(user.getuID());
            if (violations.size() > 0) {
                ArrayList<String> validationMessages = new ArrayList<String>();
                for (ConstraintViolation<User> violation : violations) {
                    validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
                }
                return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
            }
            //check if the Id is not taken
            //and create a new user
            if (u == null) {
                user.setuID(id);
                UserDB.createUser(id, user);
                return Response.ok("User Succesfully Created").build();
            }else
                return Response.status(Status.NOT_FOUND).entity("User exist already").build();
        } //end of if (u == null)
        else{
            return Response.ok("The User ID " +id +" in the URL must be the same like the user ID " + user.getuID() +" in the Body").build();
        }

    }


    @POST
    @Path("/login/{id}")
    public Response login(@PathParam("id") Integer id, User user) throws URISyntaxException {
        // validation
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        User u = UserDB.getUser(user.getuID());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<User> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        if (u != null) {
            boolean isLogin=false;
            isLogin=UserDB.login(user.getuID(),user);
            if(isLogin==true){
                return Response.ok("You have sucessfully login")
                        .build();
            }else{
                return Response.ok("Password is incorrect")
                        .build();
            }

        } else
            return Response.status(Status.NOT_FOUND).entity(id +" User does not exist.").build();
    }

}
