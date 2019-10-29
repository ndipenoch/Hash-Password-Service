package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordClient {
    private static final Logger logger =
            Logger.getLogger(PasswordClient.class.getName());
    private final ManagedChannel channel;
    // private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService;
    private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPassowrdService;

    public PasswordClient(String host, int port) {
        channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        // asyncPasswordService = PasswordServiceGrpc.newBlockingStub(channel);
        syncPassowrdService = PasswordServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public HashResponse hashUserpwd(HashRequest newHashRequest) {
        logger.info("Adding new password" + newHashRequest);
        HashResponse result = null;
        //ResponseHash result = BoolValue.newBuilder().setValue(false).build();
        try {
            result = syncPassowrdService.hash(newHashRequest);
            logger.info("Good password " + result.getHashedPassword());
            //logger.info("Back from the Salt" + result.getSalt());
            //logger.info("Back from the UserId" + result.getUserId());
        } catch (StatusRuntimeException ex) {
            logger.log(Level.WARNING, "RPC failed: {0}", ex.getStatus());
            return result;
        }
        if (result !=null) {
            logger.info("Successfully added " + newHashRequest);
        } else {
            logger.warning("Failed to add ");
        }

        return result;
    }

    private boolean checkValidation(ValidateRequest requestValidate) {

        boolean validationResponse = false;

        try {
            logger.info("Requesting all items ");

            validationResponse = syncPassowrdService.validate(requestValidate).getValue();
            logger.info("Returned from requesting all items ");
            return validationResponse;
        } catch (
                StatusRuntimeException ex) {
            logger.log(Level.WARNING, "RPC failed: {0}", ex.getStatus());
        }

        return validationResponse;
    }

    public static void main(String[] args) throws Exception {
        PasswordClient client = new PasswordClient("localhost", 50551);
        String password = "hello";
        HashRequest newDetails = HashRequest.newBuilder()
                .setUserId(007)
                .setPassword(password)
                .build();
        boolean isValid = false;
        try {
            //Sent request to hash password and get a response
            HashResponse responseHash = client.hashUserpwd(newDetails);

            //Build a validate request to send a validation request
            ValidateRequest requestValidate = ValidateRequest.newBuilder()
                    .setHashedPassword(responseHash.getHashedPassword())
                    .setPassword(password)
                    .setSalt(responseHash.getSalt())
                    .build();

            //Send a validation request and get response which is a boolean value(Another way of doing it)
            //isValid = client.syncPassowrdService.validate(requestValidate).getValue();

            //Use method to validate request and get a bool value
            isValid = client.checkValidation(requestValidate);

            System.out.println("Is Valid "+ isValid);
            //System.out.println("We got back Hash Password"+ responseHash.getHashedPassword() );
            //System.out.println("We got back Hash salt"+ responseHash.getSalt());
            // client.getItems();
        } finally {
            // Don't stop process, keep alive to receive async response
            Thread.currentThread().join();
        }
    }
}
