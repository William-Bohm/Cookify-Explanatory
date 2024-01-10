# VideoService Microservice

The VideoService is a critical component of our platform, providing robust video streaming, uploading, and transcoding capabilities. It is built with reactive Spring framework to provide a highly efficient, scalable, and asynchronous service.

## Features

- **Video Storage:** Videos are securely stored in AWS S3 buckets.
- **Transcoding:** Upon upload, each video is transcoded into four different qualities for optimal streaming performance across different network conditions.
- **Video Streaming:** Clients request specific byte ranges representing a length of time of the video, allowing for efficient video streaming.
- **Metadata Management:** All video metadata, including video length, file size, last modified time, and more, is stored in MongoDB.

## Areas for Improvement

The following enhancements are planned for the VideoService:

- **External Transcoding Service:** To optimize resource utilization and prevent server backup, we aim to offload the transcoding and uploading of different video qualities to a third-party service.
- **Adjustable Byte Range:** Currently, the requested byte range approximates 10 seconds. We are considering increasing this to provide a buffer against network failures, outages, or packet loss.

Please refer to our GitHub issues or the project roadmap for more details on these future improvements.


## Important Notice

For the security and privacy protection during the application deployment, several files and some sections of the code have been intentionally left blank or removed. These include:

- `application.properties`
- `cookify-truststore.jks`
- `public-key.pem`
- Connection information to external services within the main code

Please note, while this application currently utilizes JWT authentication, it is planned to switch to OAuth2 authentication method at the time of deployment. This decision is driven by the need to enhance the security of the application, as exposing JWT code publicly compromises the application's security.
