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
        users.put(1, new User(1, "Mark", "mark@gmit.ie"));
        users.put(2, new User(2, "James",  "james@gmail.com"));
    }

    //List all users
    public static List<User> getUsers(){
        return new ArrayList<User>(users.values());
    }

    //Get a user by Id
    public static User getUser(Integer id){
        return users.get(id);
    }

    //Update a user
    public static void updateUser(Integer id, User user){
        users.put(id, user);
    }


    //Create a user
    public static void createUser(final Integer id, final User user){
        PasswordClient client = new PasswordClient("localhost", 50551);

        HashRequest hashedUerDetails = HashRequest.newBuilder()
                .setUserId(id)
                .setPassword(user.getuPwd())
                .build();
        try {
            //Sent request  an to hash password with a callback function
            StreamObserver<HashResponse>  callback = new StreamObserver<HashResponse>() {
                User newUser;
                //Create new user
                @Override
                public void onNext(HashResponse value) {
                    newUser = new User(user.getuID(), user.getuName(), user.getuEmail(), value.getHashedPassword(), value.getSalt());
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
}
