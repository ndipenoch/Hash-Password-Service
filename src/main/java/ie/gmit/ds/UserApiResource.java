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
        User u = UserDB.getUser(user.getUid());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<User> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        if (u != null) {
            UserDB.updateUser(user.getUid(),user);
            return Response.ok("User id "+user.getUid()+" is sucessfully updated")
                    .build();
        }else
            return Response.status(Status.NOT_FOUND).entity("Can not update. User id "+user.getUid()+" does not exist").build();
    }


    @PUT
    @Path("/{id}")
    public Response createUserById(@PathParam("id") Integer id, User user) {
        // validation
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if(user.getUid().equals(id)){
            //Get user from the database
            User u = UserDB.getUser(user.getUid());
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
                user.setUid(id);
                UserDB.createUser(id, user);
                return Response.ok("User Succesfully Created").build();
            }else
                return Response.status(Status.NOT_FOUND).entity("User exist already").build();
        } //end of if (u == null)
        else{
            return Response.ok("The User ID " +id +" in the URL must be the same like the user ID " + user.getUid() +" in the Body").build();
        }

    }

    @POST
    @Path("/login")
    public Response login(User user) throws URISyntaxException {
        // validation
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        User u = UserDB.getUser(user.getUid());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<User> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        if (u != null) {
            boolean isLogin=false;
            isLogin=UserDB.login(user.getUid(),user);
            if(isLogin==true){
                return Response.ok(" You are sucessfully login")
                        .build();
            }else{
                return Response.ok(" Password is incorrect")
                        .build();
            }

        } else
            return Response.status(Status.NOT_FOUND).entity(user.getUid() +" User does not exist.").build();
    }


    @DELETE
    @Path("/{id}")
    public Response removeUserById(@PathParam("id") Integer id) {
        User user = UserDB.getUser(id);
        if (user!= null) {
            UserDB.removeUser(id);
            return Response.ok("ID "+user.getUid()+" was succesfully deleted.").build();
        }
        else
            return Response.status(Status.NOT_FOUND ).entity(id +" does not exist.").build();
    }
}