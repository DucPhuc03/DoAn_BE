package do_an.traodoido.controller;

import do_an.traodoido.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
@Tag(name = "S3 Storage", description = "API upload file lên Amazon S3")
@SecurityRequirement(name = "Bearer Authentication")
public class S3Controller {
    private final S3Service s3Service;
    
    @Operation(summary = "Upload một file lên S3", description = "Upload một file đơn lẻ lên Amazon S3 và trả về URL. Yêu cầu xác thực.")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng chọn file để upload.");
        }
        try {
            String fileUrl = s3Service.uploadFile(file);
            return ResponseEntity.ok("Upload thành công. URL: " + fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Upload thất bại: " + e.getMessage());
        }
    }

    @Operation(summary = "Upload nhiều file lên S3", description = "Upload nhiều file cùng lúc lên Amazon S3 và trả về danh sách URL. Có thể chỉ định thư mục lưu trữ. Yêu cầu xác thực.")
    @PostMapping("/upload/multi")
    public ResponseEntity<?> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "folder", required = false) String folder
    ) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Vui lòng chọn file để upload."));
        }
        try {
            List<String> urls = (folder == null || folder.trim().isEmpty())
                    ? s3Service.uploadFiles(Arrays.asList(files))
                    : s3Service.uploadFiles(Arrays.asList(files), folder);
            return ResponseEntity.ok(urls);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Upload thất bại: " + e.getMessage()));
        }
    }

}
