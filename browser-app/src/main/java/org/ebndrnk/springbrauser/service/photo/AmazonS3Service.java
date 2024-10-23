package org.ebndrnk.springbrauser.service.photo;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.springbrauser.service.user_actions.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {
    private final AmazonS3 amazonS3;
    private final UserService userService;
    @Value("${application.bucket.name}")
    private String BUCKET_NAME;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = userService.getCurrentUserId() + "_" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, fileName, inputStream, metadata));
        }

        return amazonS3.getUrl(BUCKET_NAME, fileName).toString();
    }

    public byte[] downloadFile(String url) throws IOException {
        String fileName = extractFileNameFromUrl(url);

        S3Object s3Object = amazonS3.getObject(BUCKET_NAME, fileName);
        InputStream inputStream = s3Object.getObjectContent();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);

        return outputStream.toByteArray();
    }

    public void deleteFile(String url) {
        String fileName = extractFileNameFromUrl(url);
        amazonS3.deleteObject(BUCKET_NAME, fileName);
    }

    // Вспомогательные методы
    private String generateFileName(MultipartFile multipartFile) {
        return UUID.randomUUID() + "." + getFileExtension(multipartFile);
    }

    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        int extensionIndex = fileName.lastIndexOf(".");
        if (extensionIndex > 0) {
            return fileName.substring(extensionIndex + 1);
        } else {
            return "";
        }
    }

    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
