# UserService Microservice

UserService is the backbone of our platform that manages extensive user metadata. This service stores a wide array of user data, making it a comprehensive source of information about user preferences, physical characteristics, dietary restrictions, and more. The service communicates with a MongoDB database and is built on the reactive Netty webserver, ensuring robustness, high performance, and scalability.

## User Schema

Our user data schema is designed to capture as much information about a user as possible. Here's a snapshot of the `User` document structure:

```java
public class User {
    // basic info
    @Id
    private String id;
    @NotBlank private String username, email, firstName, lastName, phoneNumber;
    private String middleName;
    
    // fitness/body
    private double weight, heightInCm;
    private ActivityLevel activityLevel;
    private Gender gender;
    private LocalDate dateOfBirth;

    // goals
    private double targetWeight;
    private WeightGoal weightGoal;

    // preferences
    private List<DietaryRestriction> dietaryRestrictions;
    private List<Cuisine> likedCuisine, dislikedCuisine;
    private List<String> likedRecipes, dislikedRecipes, cookedRecipes, savedRecipes;

    // social
    private ProfileAvailability profileAvailability;
    private List<String> followingList, followerList, uploadedRecipes, uploadedVideos;
    private String profileImage, bio;

    // data
    private LocalDate joinDate;
    private Instant lastLogin, lastPasswordChangeDate;
    private VerificationStatus verificationStatus;
    private Locale preferredLanguage;
    private Boolean mobileNotifications, emailList;
    private AccountStatus accountStatus;
    private ZoneId timeZone;

    // SocialMedia
    private List<SocialMediaProfile> socialMediaProfiles;
}
```java

# Service Capabilities
UserService offers endpoints for bulk update/upload of data. This approach is employed due to the substantial amount of data stored per user. These endpoints allow multiple values within the same general category to be updated simultaneously.

# Areas for improvement
While UserService currently performs its tasks effectively, there are opportunities for performance and functionality improvements:
* Caching: Implement a caching mechanism for popular or frequently accessed user data to enhance retrieval speed and reduce database load.

* Individual Update Endpoints: Add endpoints to allow updating of each individual value in the user schema for more granular data management.


## Important Notice

For the security and privacy protection during the application deployment, several files and some sections of the code have been intentionally left blank or removed. These include:

- `application.properties`
- `cookify-truststore.jks`
- `public-key.pem`
- Connection information to external services within the main code

Please note, while this application currently utilizes JWT authentication, it is planned to switch to OAuth2 authentication method at the time of deployment. This decision is driven by the need to enhance the security of the application, as exposing JWT code publicly compromises the application's security.
