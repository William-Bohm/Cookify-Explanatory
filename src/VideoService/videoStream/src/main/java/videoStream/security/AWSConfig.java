package videoStream.security;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfig {
    @Value("${aws.region}")
    private String region;
//    if this fails, you should check if the credentials provider is able to get them
    @Bean
    public S3Client s3Client() {
        Dotenv dotenv = Dotenv.configure().load();
        AwsCredentials awsCredentials = AwsBasicCredentials.create(dotenv.get("AWS_ACCESS_KEY_ID"), dotenv.get("AWS_SECRET_ACCESS_KEY"));

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}