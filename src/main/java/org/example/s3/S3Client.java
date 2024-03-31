package org.example.s3;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.example.supers.Utils;

import java.util.List;

public class S3Client extends Utils {

    protected AmazonS3 s3Client;
    protected final String BUCKET_NAME = "repair-shop-images";

    public S3Client() {
        ProfileCredentialsProvider credentials = new ProfileCredentialsProvider("s3");
        s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentials)
                .withRegion(Regions.EU_NORTH_1)
                .build();

        createBucket();
    }

    private void createBucket() {
        if (s3Client.doesBucketExistV2(BUCKET_NAME)) return;
        s3Client.createBucket(BUCKET_NAME);
    }

    public void emptyBucket() {
        List<S3ObjectSummary> objectSummaries = s3Client.listObjectsV2(BUCKET_NAME).getObjectSummaries();
        for (S3ObjectSummary objectSummary : objectSummaries) {
            s3Client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, objectSummary.getKey()));
        }
    }

}
