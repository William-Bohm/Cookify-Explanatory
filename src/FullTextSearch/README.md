# FullTextSearch Microservice

The FullTextSearch microservice is responsible for managing the upload of FoodItems to the Elasticsearch database. A FoodItem is a distinct string or nutrient profile of a food, which is uploaded via the client frontend at the time of recipe or ingredient upload. This service powers the autocomplete/autosuggest functionality when users are typing foods into the search bar. Authentication is performed using JWTs, and this service is equipped with the public-key to decrypt client JWTs. Each JWT contains the user ID and associated authorities.



## Important Notice

For the security and privacy protection during the application deployment, several files and some sections of the code have been intentionally left blank or removed. These include:

- `application.properties`
- `cookify-truststore.jks`
- `public-key.pem`
- Connection information to external services within the main code

Please note, while this application currently utilizes JWT authentication, it is planned to switch to OAuth2 authentication method at the time of deployment. This decision is driven by the need to enhance the security of the application, as exposing JWT code publicly compromises the application's security.
