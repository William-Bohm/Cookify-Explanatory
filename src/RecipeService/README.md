# RecipeService Microservice

The RecipeService microservice provides an extensive API for interacting with recipes. It's responsible for updating any possible attribute of a recipe, such as thumbnail IDs, tags, categories, cuisines, additional time, character set of the string, and much more. 

## Features

This service has a wide variety of endpoints for interacting with recipe data:

- **Update:** Endpoints for updating any field of a recipe document.
- **Bulk Upload/Update:** Endpoints for uploading or updating data in bulk.

All the recipe data, including the ID of the user who uploaded them (providing potential for future development of a social platform), is stored in MongoDB. 

This service is built on top of the reactive Netty webserver, providing an asynchronous and scalable solution to handle heavy loads of requests.

## Areas for Improvement

While RecipeService is already providing extensive functionalities, there are areas that can be improved:

- **Rate Limiting:** Introducing rate limiting on the free and paid tiers could help to prevent undesired behavior and misuse of the service. However, it's worth noting that HashiCorp Consul, which serves as the ingress for the Kubernetes cluster, already provides some level of protection against such issues.


## Important Notice

For the security and privacy protection during the application deployment, several files and some sections of the code have been intentionally left blank or removed. These include:

- `application.properties`
- `cookify-truststore.jks`
- `public-key.pem`
- Connection information to external services within the main code

Please note, while this application currently utilizes JWT authentication, it is planned to switch to OAuth2 authentication method at the time of deployment. This decision is driven by the need to enhance the security of the application, as exposing JWT code publicly compromises the application's security.
