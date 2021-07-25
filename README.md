# Booking API
Rest API to manage bookings in Volcano Island

##Requirements 
MySql Database must be installed

##Run Application

###With MySql
    
* Create a new schema 
* Customize application.properties with next:

    spring.datasource.url=jdbc:mysql://localhost:3306/<new_schema>
    spring.datasource.username=<user_schema_name>
    spring.datasource.password=<pass_eschema>

###Run 
    ./mvnw spring-boot:run
    
##Run Test Cases

    ./mvnw clean test

## API Documentation
You can find API endpoints documented in `{host}:{port}/api-docs.html`, where `host` and `port` values are related to where this app is running, in the case of runing it locally, try `http://localhost:8080/api-docs.html` 

## Implementation details
Stack used:
- Java 11
- Spring Boot
- Spring Data 
- MySql
- Swagger
- H2 Memory Database for testing environment

The application basically have different layers:
- **Controller**: where all the REST endpoints are defined. It's based in Spring MVC
- **Service**: middle tier to let the controllers interact with the model and the repository
- **Repository**: the repository abstraction which implementation based on Spring Data 
- **Model**: the domain. All the business rules and game logic is there.

The error handling is implemented by throwing `RuntimeExeptions` handled by `MainExceptionHandler` which is a 
 `@ControllerAdvice` that catched all uncaught exceptions before building the HTTP response. That way it's possible to
 centralize where the edge cases are handled taking advantage of SpringMVC features for converting exceptions into 
 HTTP Responses.
