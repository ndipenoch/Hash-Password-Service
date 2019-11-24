# Hash-Password-Service
Hash Password Service

Command to run the jar of part 1 in cmd: java -jar passwordservice.jar

Command to run the jar of part 2 in cmd: java -jar grpc-test-1.0-SNAPSHOT.jar server UserApiConfig.yaml

PS: To run part 2, the jar and UserApiConfig.yaml file must be in the same folder

GitHub Link: https://github.com/ndipenoch/Hash-Password-Service.git

SwaggerHub Link: https://app.swaggerhub.com/apis/ndipenoch/Password-Service-Api/1.0.0

I made test data to test the validation method in the PasswordServiceImpl in part 1 and forgot to take it off and submitted it with this data.
So, in part 2 the login was always true as the client input data were never use.
I rectified the problem later and update the PasswordServiceImpl class and resubmitted my part 1 jar file along with part 2.
