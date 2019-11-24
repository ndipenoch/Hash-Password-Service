package ie.gmit.ds;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserDB {

    //Users database
    public final static HashMap<Integer, User> users = new HashMap<>();
    static{
        //Test data
       // users.put(1, new User(1, "Mark", "mark@gmit.ie"));
    }

    //List all users
    public static List<User> getUsers(){
        return new ArrayList<User>(users.values());
    }

    //Get a user by Id
    public static User getUser(Integer id){
        return users.get(id);
    }

    public static void updateUser(final Integer id, final User user){
        PasswordClient client = new PasswordClient("localhost", 50551);

        //hashed the new details
        HashRequest hashedUerDetails = HashRequest.newBuilder()
                .setUserId(id)
                .setPassword(user.getUPwd())
                .build();
        try {
            //Sent request  an to hash password with a callback function
            StreamObserver<HashResponse>  callback = new StreamObserver<HashResponse>() {
                User newUser;
                //Update new user
                @Override
                public void onNext(HashResponse value) {
                    newUser = new User(user.getUid(), user.getUName(), user.getUEmail(), value.getHashedPassword(), value.getSalt());
                }

                @Override
                public void onError(Throwable t) {

                }

                //save the new user to the database
                @Override
                public void onCompleted() {
                    users.put(id, newUser);
                }
            };

            client.hashUserpwd(hashedUerDetails, callback);

        } finally {

        }

    }

    //Create a user
    public static void createUser(final Integer id, final User user){
        PasswordClient client = new PasswordClient("localhost", 50551);

        //hash user details
        HashRequest hashedUerDetails = HashRequest.newBuilder()
                .setUserId(id)
                .setPassword(user.getUPwd())
                .build();
        try {
            //Sent request  an to hash password with a callback function
            StreamObserver<HashResponse>  callback = new StreamObserver<HashResponse>() {
                User newUser;
                //Create new user
                @Override
                public void onNext(HashResponse value) {
                    newUser = new User(user.getUid(), user.getUName(), user.getUEmail(), value.getHashedPassword(), value.getSalt());
                }

                @Override
                public void onError(Throwable t) {

                }

                //save the user to the database
                @Override
                public void onCompleted() {
                    users.put(id, newUser);
                }
            };

            client.hashUserpwd(hashedUerDetails, callback);

        } finally {

        }

    }

    //login
    public static boolean login(Integer id, User user){
        PasswordClient client = new PasswordClient("localhost", 50551);
        boolean isValid = false;
        try {
            //Get user from the database by user ID.
            User u = getUser(id);
            //Build a validate request to send a validation request
            ValidateRequest requestValidate = ValidateRequest.newBuilder()
                    .setHashedPassword(u.getHashPwd())
                    .setPassword(user.getUPwd())
                    .setSalt(u.getSalt())
                    .build();

            //Send a validation request and get response which is a boolean value(Another way of doing it)
            //isValid = client.syncPassowrdService.validate(requestValidate).getValue();

            //Send a validation request and get response which is a boolean  value
            isValid = client.checkValidation(requestValidate);

        } finally {

        }

        return isValid;
    }

    //Delete a user
    public static void removeUser(Integer id){
        users.remove(id);
    }

}
