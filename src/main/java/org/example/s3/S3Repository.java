package org.example.s3;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.example.models.Order;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.example.s3.FileHandler.selectFileFromFolder;
import static org.example.s3.FileHandler.selectFileFromFolderAuto;

public class S3Repository extends S3Client {

    private final String fs = File.separator;
    private final String DESKTOP_PATH = System.getProperty("user.home") + fs + "Desktop";
    private final String PROJECT_PATH = System.getProperty("user.dir") + fs + "assets" + fs;
    private final String UPLOADS = PROJECT_PATH + "uploads";
    private final String DOWNLOADS = PROJECT_PATH + "downloads";
    private final String AWS_FOLDER = "images";

    public S3Repository() {
        super();
    }

    public String upload(Order order) {
        var image = selectFileFromFolder(UPLOADS);
        if (image == null) return null;

        var key = AWS_FOLDER + "/" + order.getOrderNo() + "/" + UUID.randomUUID() + "_" + image.getName();

        var request = new PutObjectRequest(BUCKET_NAME, key, image);
        s3Client.putObject(request);

        return key;
    }

    public String uploadAuto(Order order) {
        var image = selectFileFromFolderAuto(UPLOADS);
        if (image == null) return null;

        var key = AWS_FOLDER + "/" + order.getOrderNo() + "/" + UUID.randomUUID() + "_" + image.getName();

        var request = new PutObjectRequest(BUCKET_NAME, key, image);
        s3Client.putObject(request);

        return key;
    }

    public String download(String key) {
        try {
            var response = s3Client.getObject(BUCKET_NAME, key);
            var content = response.getObjectContent();

            var identityNo = extractIdentityNo(key);
            var fileName = extractFileName(key, identityNo);

            var ABSOLUTE_PATH = createFolderIfNotExists(identityNo);

            var outFile = new File(ABSOLUTE_PATH + fs + fileName);

            var os = new FileOutputStream(outFile);
            os.write(content.readAllBytes());
            os.close();

            return outFile.getAbsolutePath();
        } catch (IOException | SecurityException e) {
            System.out.println("Error downloading file: " + e.getMessage());
            return null;
        }
    }

    public List<String> downloadFolder(String orderNo) throws Exception {
        List<String> files = new ArrayList<>();
        var summaries = getS3SummariesByFolder(orderNo);
        if (summaries.isEmpty()) throw new Exception("No files downloaded...");
        for (var summary : summaries) {
            files.add(download(summary.getKey()));
        }
        return files;
    }

    public void listS3Folder(String orderNo) {
        var response = s3Client.listObjects(BUCKET_NAME, AWS_FOLDER + "/" + orderNo);
        for (var summary : response.getObjectSummaries()) {
            System.out.println(summary.getKey());
        }
    }

    public void deleteS3Folder(String orderNo) throws Exception {
        var summaries = getS3SummariesByFolder(orderNo);
        if (summaries.isEmpty()) throw new Exception("No files downloaded...");
        for (var summary : summaries) {
            s3Client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, summary.getKey()));
        }
    }

    public void deleteLocalOrderImagesFolder(String orderNo) {
        File folder = new File(DOWNLOADS + fs + orderNo);
        u.deleteLocalFolder(folder);
    }

    public void listBucket() {
        var response = s3Client.listObjects(BUCKET_NAME, AWS_FOLDER);
        for (var summary : response.getObjectSummaries()) {
            System.out.println(summary.getKey());
        }
    }

    private List<S3ObjectSummary> getS3SummariesByFolder(String orderNo) {
        var request = new ListObjectsV2Request()
                .withBucketName(BUCKET_NAME)
                .withPrefix(AWS_FOLDER + "/" + orderNo);

        return s3Client.listObjectsV2(request).getObjectSummaries();
    }

    private String createFolderIfNotExists(String folderName) {
        File folder = new File(DOWNLOADS + fs + folderName);
        if (!folder.exists()) folder.mkdirs();
        return folder.getAbsolutePath();
    }

    private String extractIdentityNo(String filePath) {
        int startIndex = filePath.indexOf(AWS_FOLDER) + AWS_FOLDER.length() + 1;
        int endIndex = filePath.indexOf("/", startIndex);
        if (startIndex != -1 && endIndex != -1) {
            return filePath.substring(startIndex, endIndex);
        }
        return "";
    }

    private String extractFileName(String filePath, String identityNo) {
        int startIndex = filePath.indexOf(identityNo) + identityNo.length() + 1;
        if (startIndex != -1) {
            return filePath.substring(startIndex);
        }
        return "";
    }

}
