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
}
