# PhotoService Microservice

The PhotoService microservice is responsible for uploading and retrieving photos from an AWS S3 bucket. This service is essential in managing images related to a Recipe, a User, or a FoodItem, with their associated IDs.

## Features

This service provides the following main features:

- **Image Upload:** Handles uploading of images to an AWS S3 bucket.
- **Image Retrieval:** Manages the retrieval of images stored in the S3 bucket.
- **Image MetaData Retrieval:** Provides access to the metadata of the stored images.

All of the metadata for the images is stored in MongoDB. The corresponding Recipe and User objects store the image ID in separate MongoDB tables. The image URL is then retrieved from the image metadata in MongoDB and sent to the client.

This service is built on top of the reactive Netty web server, making it highly scalable due to its asynchronous nature.

## Areas for Improvement

There are several areas where this service can be enhanced:

- **Transcoding Service:** Implement a transcoding service that stores lower-resolution images for situations where the connection is poor. This could be implemented using AWS Lambda functions during times when the server is heavily loaded, else computed on non-busy threads.
- **Image URL Storage:** Instead of making an additional database call to retrieve the image URL, store the URL along with the Image ID in the Recipe and User documents in MongoDB.



## Important Notice

For the security and privacy protection during the application deployment, several files and some sections of the code have been intentionally left blank or removed. These include:

- `application.properties`
- `cookify-truststore.jks`
- `public-key.pem`
- Connection information to external services within the main code

Please note, while this application currently utilizes JWT authentication, it is planned to switch to OAuth2 authentication method at the time of deployment. This decision is driven by the need to enhance the security of the application, as exposing JWT code publicly compromises the application's security.
