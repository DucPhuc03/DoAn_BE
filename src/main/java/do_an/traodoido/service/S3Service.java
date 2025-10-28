package do_an.traodoido.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3Service {
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public S3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Tên file trên S3. Nên dùng UUID để đảm bảo tên là duy nhất
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Thiết lập metadata cho file
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            // Tạo request và upload file
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName, fileName, file.getInputStream(), metadata
            );
            s3Client.putObject(putObjectRequest);

            // Trả về URL của file đã upload (tùy chọn)
            return s3Client.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            // Xử lý lỗi
            throw new IOException("Không thể upload file lên S3", e);
        }
    }

    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> uploadedUrls = new ArrayList<>();
        if (files == null || files.isEmpty()) {
            return uploadedUrls;
        }
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String url = uploadFile(file);
                uploadedUrls.add(url);
            }
        }
        return uploadedUrls;
    }

    private String buildObjectKey(String folder, String originalFilename) {
        String safeFolder = folder == null ? "" : folder.trim();
        if (safeFolder.startsWith("/")) {
            safeFolder = safeFolder.substring(1);
        }
        if (safeFolder.endsWith("/")) {
            safeFolder = safeFolder.substring(0, safeFolder.length() - 1);
        }
        String generatedName = System.currentTimeMillis() + "_" + originalFilename;
        return safeFolder.isEmpty() ? generatedName : safeFolder + "/" + generatedName;
    }

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        String objectKey = buildObjectKey(folder, file.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName, objectKey, file.getInputStream(), metadata
            );
            s3Client.putObject(putObjectRequest);
            return s3Client.getUrl(bucketName, objectKey).toString();
        } catch (IOException e) {
            throw new IOException("Không thể upload file lên S3", e);
        }
    }

    public List<String> uploadFiles(List<MultipartFile> files, String folder) throws IOException {
        List<String> uploadedUrls = new ArrayList<>();
        if (files == null || files.isEmpty()) {
            return uploadedUrls;
        }
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String url = uploadFile(file, folder);
                uploadedUrls.add(url);
            }
        }
        return uploadedUrls;
    }
}
