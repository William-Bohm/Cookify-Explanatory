package videoStream.services;

import org.springframework.scheduling.annotation.Async;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.ByteBuffer;

import videoStream.services.dataModels.VideoUploadResult;
import ws.schild.jave.*;
import org.springframework.beans.factory.annotation.Value;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.VideoSize;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class TranscodeService {
    private static final int[] videoWidths = {1280, 854, 640, 426};

    @Value("${videoService.maxFileSize}")
    private long maxFileSize;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private VideoUploadService videoUploadService;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;


    public void transcodeAndUpload(String s3ObjectKey) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3ObjectKey)
                .build();

        File inputFile = File.createTempFile("input", ".mp4");
        s3Client.getObject(getObjectRequest, inputFile.toPath());

        // Check file size
        if (inputFile.length() > maxFileSize) {
            throw new IllegalArgumentException("File size is too large");
        }

        for (int videoWidth : videoWidths) {
            File outputFile = transcodeVideo(inputFile, videoWidth);
            VideoUploadResult result = videoUploadService.uploadTranscodedCopy(outputFile).block();
            outputFile.delete(); // Delete temporary output file

            // Check if upload was successful
            if (!result.getResponse().sdkHttpResponse().isSuccessful()) {
                throw new RuntimeException("Upload failed for video width " + videoWidth);
            }
        }

        inputFile.delete(); // Delete temporary input file
    }

    @Async
    public void asyncTranscodeAndUpload(String s3ObjectKey) {
        File inputFile = null;
        File outputFile = null;
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3ObjectKey)
                    .build();

            inputFile = File.createTempFile("input", ".mp4");
            s3Client.getObject(getObjectRequest, inputFile.toPath());

            // Check file size
            if (inputFile.length() > maxFileSize) {
                throw new IllegalArgumentException("File size is too large");
            }


            for (int videoWidth : videoWidths) {
                outputFile = transcodeVideo(inputFile, videoWidth);
                videoUploadService.uploadVideo(outputFile);
                outputFile.delete(); // Delete temporary output file immediately after it's no longer needed
            }
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while handling file operations", e);
        } catch (EncoderException e) {
            throw new RuntimeException("Error occurred while transcoding video", e);
        } finally {
            if (inputFile != null) inputFile.delete(); // Ensure input file is deleted, even if an error occurs
            if (outputFile != null) outputFile.delete(); // Ensure output file is deleted, even if an error occurs
        }
    }

    private File transcodeVideo(File inputFile, int videoWidth) throws IOException {
        File outputFile = File.createTempFile("output", ".mp4");
        try {
            MultimediaObject multimediaObject = new MultimediaObject(inputFile);

            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("aac");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            VideoAttributes video = new VideoAttributes();
            video.setCodec("libx264");
            video.setSize(new VideoSize(videoWidth, 0)); // Set the width and let the library calculate the height
            video.setBitRate(800000);

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("mp4");
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);

            Encoder encoder = new Encoder();
            encoder.encode(multimediaObject, outputFile, attrs);
        } catch (EncoderException e) {
            throw new RuntimeException("Error occurred while transcoding video", e);
        }
        return outputFile;
    }
}
