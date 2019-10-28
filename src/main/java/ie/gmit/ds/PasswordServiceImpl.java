package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;


public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {
    private static final Logger logger =
            Logger.getLogger(PasswordServiceImpl.class.getName());


    byte[] salt_2;
    byte[] hashedPassword_2;


    @Override
    public void hash(HashRequest request, StreamObserver<HashResponse> responseObserver) {

        try {

            String pwd = request.getPassword();
            char[] pwdCharArray = pwd.toCharArray();
            byte[] salt = Passwords.getNextSalt();
            byte[] hashedPassword  = Passwords.hash(pwdCharArray,salt);

            //Build object
            HashResponse responseHash = HashResponse.newBuilder()
                    .setSalt(ByteString.copyFrom(salt))
                    .setUserId(request.getUserId())
                    .setHashedPassword(ByteString.copyFrom(hashedPassword))
                    .build();

            responseObserver.onNext(responseHash);
        } catch (RuntimeException ex) {
            responseObserver.onNext(null);
        }
        responseObserver.onCompleted();

    }

    @Override
    public void validate(ValidateRequest request, StreamObserver<BoolValue> responseObserver) {
        try {

            String pwd = request.getPassword();
            char[] pwdCharArray = pwd.toCharArray();
            byte[] salt = Passwords.getNextSalt();
            byte[] hashedPassword  = Passwords.hash(pwdCharArray,salt);

            logger.info("Checking the validity of password " );
            boolean validationRequest = Passwords.isExpectedPassword(pwdCharArray,salt,hashedPassword );
            if(validationRequest==true){
                responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
            }else{
                responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
            }
        } catch (RuntimeException ex) {
            responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
        }
        responseObserver.onCompleted();
    }

}
