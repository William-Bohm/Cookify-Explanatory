# RecommendationService Microservice

The RecommendationService is a key part of our platform that provides personalized recipe recommendations to users. This service works by interacting with a graph database (Neo4j) to create a web of interrelations between users, dietary restrictions, ingredients, and recipes. 

## Features

This service offers various endpoints for managing data in the recommendation graph:

- **Add Dietary Restrictions:** Update dietary restrictions for a user.
- **Add Ingredients:** Add new ingredients to the graph.
- **Add Recipes:** Add new recipes to the graph.
- **Add Users:** Add new users to the graph.

Based on these relationships, the service recommends recipes to a user that they haven't disliked and are not against their dietary restrictions. Recommendations are made based on similarity in ingredients with the recipes the user has previously liked and ensures these recipes have not been recommended recently.

## Areas for Improvement

The RecommendationService, while functional, has several areas that could be improved to enhance performance and the quality of recommendations:

- **Performance Testing:** Carry out performance testing on various recommendation algorithms to identify the most efficient and accurate ones.
- **Quality Testing:** Test the quality of recommendations using different combinations of node types.
- **Reactive Programming:** The service was not designed using a reactive style. Transitioning to a reactive programming model could significantly improve the performance and scalability of the service.


## Important Notice

For the security and privacy protection during the application deployment, several files and some sections of the code have been intentionally left blank or removed. These include:

- `application.properties`
- `cookify-truststore.jks`
- `public-key.pem`
- Connection information to external services within the main code

Please note, while this application currently utilizes JWT authentication, it is planned to switch to OAuth2 authentication method at the time of deployment. This decision is driven by the need to enhance the security of the application, as exposing JWT code publicly compromises the application's security.
