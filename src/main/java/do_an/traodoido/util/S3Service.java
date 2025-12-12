package do_an.traodoido.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
    private final Gson gson = new Gson();
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


    public void saveItemVector(Long itemId, List<Float> vector) {
        if (itemId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "itemId rỗng");
        if (vector == null || vector.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vector rỗng");

        JsonObject json = new JsonObject();
        json.addProperty("id", itemId);
        json.addProperty("type", "post");
        json.addProperty("dimensions", vector.size());

        JsonArray arr = new JsonArray();
        vector.forEach(arr::add);
        json.add("vector", arr);

        String jsonString = gson.toJson(json);
        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);

        String objectKey = "embedding_post/" + itemId + ".json";

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/json");
        metadata.setContentLength(bytes.length);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {

            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, objectKey, inputStream, metadata);

            s3Client.putObject(putObjectRequest);   // SDK V1 call

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể lưu vector lên S3", e);
        }
    }

    public List<Float> loadPostVector(Long postId) {
        if (postId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "postId rỗng");
        }

        String objectKey = "embedding_post/" + postId + ".json";

        try {
            // Kiểm tra xem file có tồn tại không
            if (!s3Client.doesObjectExist(bucketName, objectKey)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy vector cho postId: " + postId);
            }

            // Lấy object từ S3
            S3Object s3Object = s3Client.getObject(bucketName, objectKey);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();

            // Đọc và parse JSON
            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                JsonArray vectorArray = json.getAsJsonArray("vector");

                List<Float> vector = new ArrayList<>();
                for (int i = 0; i < vectorArray.size(); i++) {
                    vector.add(vectorArray.get(i).getAsFloat());
                }

                return vector;
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể đọc vector từ S3", e);
        }
    }


}
