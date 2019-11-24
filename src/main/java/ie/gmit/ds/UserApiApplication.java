package ie.gmit.ds;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class UserApiApplication extends Application<UserApiConfig> {

    public static void main(String[] args) throws Exception {
        new UserApiApplication().run(args);
    }

    public void run(UserApiConfig u, Environment e) throws Exception {

        final UserApiResource resource = new UserApiResource(e.getValidator());

        final UserApiHealthCheck healthCheck = new UserApiHealthCheck();
        e.healthChecks().register("Health checking", healthCheck);
        e.jersey().register(resource);
    }
}